package com.ceos22.cgv_clone.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleCreateRequest(
        @NotNull(message = "영화 ID는 필수입니다.")
        Long movieId,

        @NotNull(message = "상영관 ID는 필수입니다.")
        Long screenId,

        @NotNull(message = "상영 시작 시간은 필수입니다.")
        LocalDateTime startTime,

        @NotNull(message = "상영 종료 시간은 필수입니다.")
        LocalDateTime endTime
) {
}
