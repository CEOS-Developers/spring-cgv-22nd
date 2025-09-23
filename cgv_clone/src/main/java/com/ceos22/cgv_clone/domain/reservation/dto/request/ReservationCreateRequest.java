package com.ceos22.cgv_clone.domain.reservation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReservationCreateRequest(
        @NotNull(message = "회원 ID는 필수입니다")
        Long memberId,

        @NotNull(message = "스케쥴 ID는 필수입니다")
        Long scheduleId,

        @NotNull(message = "좌석 ID는 필수입니다")
        List<Long> seatIds
) {
}
