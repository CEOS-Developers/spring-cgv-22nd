package com.ceos22.cgv_clone.domain.theater.dto.request;

import com.ceos22.cgv_clone.domain.theater.entity.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TheaterCreateRequest(
        @NotNull(message = "지역 ID는 필수입니다")
        Region region,

        @NotBlank(message = "영화관 이름은 필수입니다.")
        String name,

        @NotBlank(message = "영화관 주소는 필수입니다.")
        String address
) {
}
