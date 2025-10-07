package com.ceos22.cgv.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("결제 대기"),
    PAID("결제 완료");

    private final String description;
}
