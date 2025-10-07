package com.ceos22.cgv_clone.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequest(
        @NotNull(message = "매점 ID는 필수입니다.")
        Long storeId,

        @NotNull(message = "상품 ID는 필수입니다.")
        Long productId,

        @NotNull(message = "수량은 필수입니다.")
        @Positive(message = "수량은 1개 이상이어야 합니다.")
        Integer quantity
) {
}