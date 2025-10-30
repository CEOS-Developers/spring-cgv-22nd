package com.ceos22.cgv_clone.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
        @NotNull(message = "매점 ID는 필수입니다.")
        Long storeId
) {
}
