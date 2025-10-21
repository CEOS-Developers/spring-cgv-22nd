package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategory {
    COMBO("콤보"),
    POPCORN("팝콘"),
    DRINK("음료"),
    SNACK("스낵"),
    GOODS("굿즈");

    private final String description;
}
