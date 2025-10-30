package com.ceos22.cgv_clone.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    APP_CARD("앱카드"),
    PHONE("휴대폰 결제"),
    PAYCO("페이코"),
    NAVER_PAY("네이버 페이"),
    TOSS_PAY("토스 페이"),
    KAKAO_PAY("카카오 페이");

    private final String description;
}
