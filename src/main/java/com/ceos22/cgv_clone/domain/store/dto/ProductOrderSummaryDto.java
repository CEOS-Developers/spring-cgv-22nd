package com.ceos22.cgv_clone.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProductOrderSummaryDto {

    private Long orderId;
    private Long cinemaId;
    private Integer totalPrice;
    private LocalDateTime createdAt;
}
