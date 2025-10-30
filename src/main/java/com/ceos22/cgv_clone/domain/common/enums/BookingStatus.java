package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    BOOKED("예약"),
    CANCELED("예약 취소");

    private final String description;
}
