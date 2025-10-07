package com.ceos22.cgv_clone.domain.store.dto.request;

import com.ceos22.cgv_clone.domain.store.entity.StoreType;
import jakarta.validation.constraints.NotNull;

public record StoreCreateRequest(
        @NotNull(message = "매점 타입은 필수입니다.")
        StoreType storeType,

        @NotNull(message = "영화관 ID는 필수입니다.")
        Long theaterId
) {
}
