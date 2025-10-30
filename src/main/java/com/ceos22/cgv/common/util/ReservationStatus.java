package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    HOLD("예약 대기"),
    RESERVED("예약 완료"),
    CANCELLED("예약 취소"),
    FAILED("결제 실패");

    private final String description;

}
