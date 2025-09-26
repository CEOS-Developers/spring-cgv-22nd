package com.ceos22.cgv.module.snack.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long orderId,
    Long cinemaId,
    Long userId,
    Integer totalPrice,
    LocalDateTime createdAt,
    List<OrderItemResponse> items){
}
