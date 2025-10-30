package com.ceos22.cgv_clone.domain.order.dto.response;

import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import com.ceos22.cgv_clone.domain.store.entity.StoreType;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        Long storeId,
        StoreType storeType,
        OrderStatus status,
        Integer totalPrice,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getOrderDetails().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(
                order.getOrderId(),
                order.getStore().getStoreId(),
                order.getStore().getStoreType(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                items
        );
    }
}
