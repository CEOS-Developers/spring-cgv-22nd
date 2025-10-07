package com.ceos22.cgv_clone.domain.order.dto.response;

import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import com.ceos22.cgv_clone.domain.store.entity.StoreType;

import java.util.List;

public record CartResponse(
        Long cartId,
        Long storeId,
        StoreType storeType,
        Integer totalPrice,
        List<CartItemResponse> items
) {
    public static CartResponse from(Order cart) {
        if (!cart.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalArgumentException("장바구니 상태가 아닙니다.");
        }

        List<CartItemResponse> items = cart.getOrderDetails().stream()
                .map(CartItemResponse::from)
                .toList();

        return new CartResponse(
                cart.getOrderId(),
                cart.getStore().getStoreId(),
                cart.getStore().getStoreType(),
                cart.getTotalPrice(),
                items
        );
    }
}
