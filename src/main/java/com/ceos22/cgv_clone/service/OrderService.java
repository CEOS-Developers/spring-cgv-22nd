package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.User;
import com.ceos22.cgv_clone.domain.shop.Order;
import com.ceos22.cgv_clone.domain.shop.OrderItem;
import com.ceos22.cgv_clone.domain.shop.Stock;
import com.ceos22.cgv_clone.domain.theater.Theater;
import com.ceos22.cgv_clone.dto.request.OrderItemRequestDto;
import com.ceos22.cgv_clone.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.dto.response.OrderItemResponseDto;
import com.ceos22.cgv_clone.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final StockRepository stockRepository;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new IllegalArgumentException("매점 없음"));

        // OrderItem에 Order를 넣어주기 위해 미리 build
        Order order = Order.builder()
                .user(user)
                .theater(theater)
                .totalPrice(0)
                .build();
        ordersRepository.save(order);

        int totalPrice = 0;
        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItemRequestDto item : request.getItems()) {
            Stock stock = stockRepository.findByProduct_IdAndTheater_Id(item.getProductId(), request.getTheaterId())
                    .orElseThrow(() -> new IllegalArgumentException("재고 없음"));

            // 주문 수량이 재고보다 많을 때의 예외 처리
            if (stock.getStock() < item.getQuantity()) {
                throw new IllegalStateException("재고 부족");
            }

            // 재고 차감
            stock.setStock(stock.getStock() - item.getQuantity());

            // 주문 상품(상세) 생성
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(stock.getProduct())
                    .quantity(item.getQuantity())
                    // 상품 가격 * 수량
                    .price(stock.getProduct().getPrice() * item.getQuantity())
                    .build();

            // 주문 상품(상세) 저장
            orderItemRepository.save(orderItem);

            // 총 가격 계산
            totalPrice += orderItem.getPrice();

            // Dto 변환해서 리스트에 추가
            itemDtos.add(OrderItemResponseDto.fromEntity(orderItem));
        }

        order.setTotalPrice(totalPrice);

        return OrderResponseDto.fromEntity(order, itemDtos);
    }

    //주문 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String username, Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new SecurityException("본인의 주문만 조회할 수 있습니다.");
        }

        List<OrderItemResponseDto> items = orderItemRepository.findByOrder_Id(orderId)
                .stream()
                .map(OrderItemResponseDto::fromEntity)
                .toList();

        return OrderResponseDto.fromEntity(order, items);
    }
}
