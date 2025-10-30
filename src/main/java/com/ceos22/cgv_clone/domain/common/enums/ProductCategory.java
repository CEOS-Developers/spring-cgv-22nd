package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
    COMBO("콤보"),
    POPCORN("팝콘"),
    DRINK("음료"),
    SNACK("스낵");

    private final String description;
}
