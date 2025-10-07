package com.ceos22.cgv.module.snack.service;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.cinema.repository.CinemaRepository;
import com.ceos22.cgv.module.snack.domain.Menu;
import com.ceos22.cgv.module.snack.domain.Order;
import com.ceos22.cgv.module.snack.domain.OrderItem;
import com.ceos22.cgv.module.snack.dto.OrderItemResponse;
import com.ceos22.cgv.module.snack.dto.OrderRequest;
import com.ceos22.cgv.module.snack.dto.OrderResponse;
import com.ceos22.cgv.module.snack.repository.MenuRepository;
import com.ceos22.cgv.module.snack.repository.OrderItemRepository;
import com.ceos22.cgv.module.snack.repository.OrderRepository;
import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.module.user.repository.UserRepository;
import com.ceos22.cgv.util.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CinemaRepository cinemaRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponse order(OrderRequest request, Long userId) {

        // 연관 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        Cinema cinema = cinemaRepository.findById(request.cinemaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "영화관을 찾을 수 없습니다."));

        List<Long> menuIds = request.items().stream().map(OrderRequest.OrderItemRequest::menuId).toList();

        Map<Long, Menu> menuMap = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getMenuId, menu -> menu));

        // Order 생성 (PENDING)
        Order order = Order.builder()
                .cinema(cinema)
                .user(user)
                .status(OrderStatus.PAID) // 결제 상태는 고려하지 않음
                .totalPrice(0) // 이후 계산
                .build();
        orderRepository.save(order);

        // OrderItem 생성 + 합계 계산
        int total = 0;
        List<OrderItem> savedItems = new ArrayList<>();
        for (OrderRequest.OrderItemRequest it : request.items()) {
            Menu menu = menuMap.get(it.menuId());
            int unitPrice = menu.getPrice();
            int lineTotal = unitPrice * it.quantity();
            total += lineTotal;

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .menu(menu)
                    .quantity(it.quantity())
                    .unitPrice(unitPrice)
                    .build();
            savedItems.add(item);
        }
        orderItemRepository.saveAll(savedItems);

        // 합계 가격 반영
        order.changeTotalPrice(total);

        return toResponse(order, savedItems);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId, Long userId) {

        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 주문에 접근할 수 없습니다.");
        }
        return toResponse(order, order.getItems());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> myOrders(Long userId) {
        List<Order> orders = orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        return orders.stream()
                .map(o -> toResponse(o, o.getItems()))
                .toList();
    }

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemDtos = items.stream().map(oi ->
                new OrderItemResponse(
                        oi.getId(),
                        oi.getMenu().getMenuId(),
                        oi.getMenu().getName(),
                        oi.getUnitPrice(),
                        oi.getQuantity(),
                        oi.getUnitPrice() * oi.getQuantity()
                )
        ).toList();

        return new OrderResponse(
                order.getId(),
                order.getCinema().getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                itemDtos
        );
    }
}
