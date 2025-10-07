package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketPrice {

    ADULT_PRICE(15000, "성인"),
    TEEN_PRICE(11000, "청소년");

    private final int price;
    private final String description;
}
