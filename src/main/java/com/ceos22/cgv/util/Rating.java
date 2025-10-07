package com.ceos22.cgv.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Rating {
    ALL("전체관람가"),
    AGE_12("12세 관람가"),
    AGE_15("15세 관람가"),
    AGE_19("청소년 관람불가");

    private final String description;
}
