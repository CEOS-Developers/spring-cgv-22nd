package com.ceos22.cgv_clone.domain.store.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record StoreStockCreateRequest(
        @NotNull(message = "매점 ID는 필수입니다.")
        Long storeId,

        @NotNull(message = "상품 ID는 필수입니다.")
        Long productId,

        @NotNull(message = "초기 재고는 필수입니다.")
        @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
        Integer initialStock
) {
}
