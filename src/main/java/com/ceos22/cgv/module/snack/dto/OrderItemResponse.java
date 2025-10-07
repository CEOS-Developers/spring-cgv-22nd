package com.ceos22.cgv.module.snack.dto;

import com.ceos22.cgv.module.snack.repository.OrderRepository;

public record OrderItemResponse(
        Long orderItemId,
        Long menuId,
        String menuName,
        Integer unitPrice,
        Integer quantity,
        Integer itemTotal
) {}