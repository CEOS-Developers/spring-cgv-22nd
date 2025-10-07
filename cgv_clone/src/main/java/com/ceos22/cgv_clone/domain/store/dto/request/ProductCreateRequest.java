package com.ceos22.cgv_clone.domain.store.dto.request;

import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ProductCreateRequest(
        @NotBlank(message = "상품명은 필수입니다.")
        String name,

        @NotNull(message = "가격은 필수입니다.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        Integer price,

        @NotNull(message = "카테고리는 필수입니다.")
        ProductCategory category,

        Boolean isCombo,

        String imageUrl,

        List<Long> comboItemIds
) {
}
