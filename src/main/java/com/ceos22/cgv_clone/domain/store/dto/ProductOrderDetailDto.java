package com.ceos22.cgv_clone.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProductOrderDetailDto {

    private Long orderId;
    private Long memberId;
    private Long cinemaId;
    private Integer totalPrice;
    private List<ProductOrderResponseDto.OrderItem> items;
}
