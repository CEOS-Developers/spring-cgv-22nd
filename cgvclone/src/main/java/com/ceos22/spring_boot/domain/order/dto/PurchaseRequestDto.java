package com.ceos22.spring_boot.domain.order.dto;

import com.ceos22.spring_boot.common.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PurchaseRequestDto(
        @NotEmpty List<Item> items,
        @NotNull PaymentMethod method
) {
    public record Item(@NotNull Long productId, @Min(1) int quantity) {}
}
