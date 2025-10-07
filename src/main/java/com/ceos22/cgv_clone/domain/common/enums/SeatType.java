package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatType {
    STANDARD("일반석"),
    DISABLED("장애인석");

    private final String type;
}
