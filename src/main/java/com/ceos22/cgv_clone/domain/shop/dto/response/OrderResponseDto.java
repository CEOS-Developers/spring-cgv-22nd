package com.ceos22.cgv_clone.domain.shop.dto.response;


import lombok.Builder;
import lombok.Getter;
import com.ceos22.cgv_clone.domain.shop.entity.Order;

import java.util.List;

@Getter
@Builder
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private Long theaterId;
    private int totalPrice;
    private List<OrderItemResponseDto> items;

    public static OrderResponseDto fromEntity(Order order, List<OrderItemResponseDto> items) {
        return OrderResponseDto.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .theaterId(order.getTheater().getId())
                .totalPrice(order.getTotalPrice())
                .items(items)
                .build();
    }
}
