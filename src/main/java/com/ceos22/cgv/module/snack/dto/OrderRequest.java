package com.ceos22.cgv.module.snack.dto;

import java.util.List;

public record OrderRequest(
        Long cinemaId,
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(Long menuId, Integer quantity) {}
}