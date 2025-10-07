package com.ceos22.cgv_clone.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private Long theaterId;
    private List<OrderItemRequestDto> items;
}
