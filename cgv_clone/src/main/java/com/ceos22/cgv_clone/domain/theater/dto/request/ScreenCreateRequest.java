package com.ceos22.cgv_clone.domain.theater.dto.request;

import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScreenCreateRequest(
        @NotBlank(message = "상영관 이름은 필수입니다")
        String name,
        
        @NotNull(message = "상영관 타입은 필수입니다")
        ScreenType type,
        
        @NotNull(message = "좌석 정보는 필수입니다")
        List<SeatCreateRequest> seats
) {
    public record SeatCreateRequest(
            @NotBlank(message = "좌석의 행 정보는 필수입니다")
            String rowName,
            @NotBlank(message = "좌석의 행 정보는 필수입니다")
            String columnNumber
    ){
    }
}
