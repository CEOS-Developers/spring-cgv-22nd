package com.ceos22.cgv_clone.domain.order.dto.response;

import com.ceos22.cgv_clone.domain.order.entity.OrderDetail;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        Integer price,
        String imageUrl,
        Integer quantity,
        Integer subtotal
) {
    public static CartItemResponse from(OrderDetail orderDetail) {
        return new CartItemResponse(
                orderDetail.getOrderDetailId(),
                orderDetail.getProduct().getProductId(),
                orderDetail.getProduct().getName(),
                orderDetail.getProduct().getPrice(),
                orderDetail.getProduct().getImageUrl(),
                orderDetail.getQuantity(),
                orderDetail.getSubtotal()
        );
    }
}
