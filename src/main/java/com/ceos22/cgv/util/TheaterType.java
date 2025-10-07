package com.ceos22.cgv.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TheaterType {
    STANDARD("일반관"),
    SPECIAL("특별관");

    private final String description;
}
