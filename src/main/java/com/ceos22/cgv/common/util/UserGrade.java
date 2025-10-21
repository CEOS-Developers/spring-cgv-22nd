package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGrade {
    NORMAL("일반"),
    VIP("VIP"),
    RVIP("RVIP"),
    VVIP("VVIP"),
    SVIP("SVIP");

    private final String description;
}
