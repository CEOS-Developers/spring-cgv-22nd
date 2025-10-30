package com.ceos22.cgv_clone.domain.order.dto.response;

import com.ceos22.cgv_clone.domain.order.entity.OrderDetail;

public record OrderItemResponse(
        Long orderDetailId,
        Long productId,
        String productName,
        Integer priceAtPurchase,
        Integer quantity,
        Integer subtotal
) {
    public static OrderItemResponse from(OrderDetail orderDetail) {
        return new OrderItemResponse(
                orderDetail.getOrderDetailId(),
                orderDetail.getProduct().getProductId(),
                orderDetail.getProduct().getName(),
                orderDetail.getProduct().getPrice(),
                orderDetail.getQuantity(),
                orderDetail.getSubtotal()
        );
    }
}
