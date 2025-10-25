package com.ceos22.cgv.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {

    FAILED("결제 실패"),
    CANCELLED("결제 취소"),
    PAID("결제 완료");

    private final String description;
}
