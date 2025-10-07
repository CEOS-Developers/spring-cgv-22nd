package com.ceos22.cgv_clone.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequestDto {
    private Long productId;
    private int quantity;
}
