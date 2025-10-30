package com.ceos22.cgv_clone.domain.shop.dto.response;

import com.ceos22.cgv_clone.domain.shop.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponseDto {
    private String productName;
    private int quantity;
    private int price;

    public static OrderItemResponseDto fromEntity(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
