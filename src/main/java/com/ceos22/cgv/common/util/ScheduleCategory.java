package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleCategory {
    MORNING("오전"),
    AFTERNOON("오후"),
    AFTER6PM("18시 이후"),
    MIDNIGHT("심야");

    private final String description;
}
