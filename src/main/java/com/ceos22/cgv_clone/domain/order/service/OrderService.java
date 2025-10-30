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

import java.util.Collections;
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
        Member member = findMemberById(memberId);
        Store store = findStoreById(request.storeId());

        Order cart = getCart(member, store);

        validateCartNotEmpty(cart);

        cart.pendPayment();

        return OrderResponse.from(cart);
    }

    @Transactional
    public void confirmOrderAfterPayment(Long orderId) {
        Order order = findOrderById(orderId);

        decreaseStockForOrder(order);

        order.completePayment();
    }

    @Transactional
    public void cancelOrderAfterPaymentCancel(Long orderId) {
        Order order = findOrderById(orderId);

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            increaseStockForOrder(order);
        }

        order.cancelPayment();
    }

    public List<OrderResponse> getMyOrders(Long memberId) {
        Member member = findMemberById(memberId);

        List<Order> allOrders = orderRepository.findByMember(member);

        List<Order> orders = allOrders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CART)
                .toList();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }


    public OrderResponse getOrderDetail(Long memberId, Long orderId) {
        Member member = findMemberById(memberId);
        Order order = findOrderById(orderId);

        validateOrderOwner(order, member);
        validateOrderNotCart(order);

        return OrderResponse.from(order);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Order getCart(Member member, Store store) {
        List<Order> carts = orderRepository.findByMemberAndStatus(member, OrderStatus.CART)
                .orElse(Collections.emptyList());

        return carts.stream()
                .filter(order -> order.getStore().getStoreId().equals(store.getStoreId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));
    }

    private void validateCartNotEmpty(Order cart) {
        if (cart.getOrderDetails().isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }
    }

    private void validateOrderOwner(Order order, Member member) {
        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }
    }

    private void validateOrderNotCart(Order order) {
        if (order.getStatus() == OrderStatus.CART) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
    }


    private void decreaseStockForOrder(Order order) {
        for (OrderDetail detail : order.getOrderDetails()) {
            try {
                storeService.decreaseStock(
                        order.getStore().getStoreId(),
                        detail.getProduct().getProductId(),
                        detail.getQuantity()
                );
            } catch (CustomException e) {
                log.error("재고 차감 실패: orderId={}, productId={}, quantity={}, error={}",
                        order.getOrderId(), detail.getProduct().getProductId(), detail.getQuantity(), e.getMessage());
                throw e;
            } catch (Exception e) {
                log.error("재고 차감 중 예상치 못한 오류 발생: orderId={}, productId={}, quantity={}",
                        order.getOrderId(), detail.getProduct().getProductId(), detail.getQuantity(), e);
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void increaseStockForOrder(Order order) {
        for (OrderDetail detail : order.getOrderDetails()) {
            try {
                storeService.increaseStock(
                        order.getStore().getStoreId(),
                        detail.getProduct().getProductId(),
                        detail.getQuantity()
                );
            } catch (Exception e) {
                log.error("재고 복구 실패: orderId={}, productId={}, quantity={}, error={}",
                        order.getOrderId(), detail.getProduct().getProductId(), detail.getQuantity(), e.getMessage());
            }
        }
    }
}