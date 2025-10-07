package com.ceos22.cgv_clone.domain.order.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.order.dto.request.OrderCreateRequest;
import com.ceos22.cgv_clone.domain.order.dto.response.OrderResponse;
import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderDetail;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import com.ceos22.cgv_clone.domain.order.repository.OrderRepository;
import com.ceos22.cgv_clone.domain.store.entity.Store;
import com.ceos22.cgv_clone.domain.store.repository.StoreRepository;
import com.ceos22.cgv_clone.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final StoreService storeService;

    @Transactional
    public OrderResponse createOrder(Long memberId, OrderCreateRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        List<Order> pendingOrders = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Order cart = pendingOrders.stream()
                .filter(order -> order.getStore().getStoreId().equals(store.getStoreId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        if (cart.getOrderDetails().isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }

        for (OrderDetail detail : cart.getOrderDetails()) {
            try {
                storeService.decreaseStock(
                        store.getStoreId(),
                        detail.getProduct().getProductId(),
                        detail.getQuantity()
                );
            } catch (Exception e) {
                throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
            }
        }

        cart.confirmOrder();

        return OrderResponse.from(cart);
    }


    @Transactional
    public void cancelOrder(Long memberId, Long orderId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }

        for (OrderDetail detail : order.getOrderDetails()) {
            try {
                storeService.increaseStock(
                        order.getStore().getStoreId(),
                        detail.getProduct().getProductId(),
                        detail.getQuantity()
                );
            } catch (Exception e) {
                log.error("재고 복구 실패: 상품ID={}, 수량={}, 오류={}",
                        detail.getProduct().getProductId(), detail.getQuantity(), e.getMessage());
                // 재고 복구를 위한 로깅
            }
        }
        order.cancelOrder();
    }


    public List<OrderResponse> getMyOrders(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<Order> allOrders = orderRepository.findByMember(member);

        List<Order> orders = allOrders.stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.PENDING)
                .collect(Collectors.toList());

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderDetail(Long memberId, Long orderId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        if (order.getOrderStatus() == OrderStatus.PENDING) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        return OrderResponse.from(order);
    }


    @Transactional
    public void completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.completeOrder();
    }
}
