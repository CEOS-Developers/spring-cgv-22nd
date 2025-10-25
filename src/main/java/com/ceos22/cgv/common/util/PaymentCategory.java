package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentCategory {
    Reservation("영화 예매"),
    SNACK("매점 구매");

    private final String description;
}
