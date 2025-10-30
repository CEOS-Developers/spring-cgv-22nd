package com.ceos22.cgv_clone.domain.theater.dto.request;

import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ScreenCreateRequest(
        @NotBlank(message = "상영관 이름은 필수입니다")
        String name,
        
        @NotNull(message = "상영관 타입은 필수입니다")
        ScreenType type,
        
        @NotNull(message = "총 행 수는 필수입니다")
        Integer totalRows,

        @NotNull(message = "총 열 수는 필수입니다")
        Integer totalCols
) {
}
