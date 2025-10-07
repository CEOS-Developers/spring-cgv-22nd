package com.ceos22.cgv_clone.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING("장바구니"),
    CONFIRMED("주문 완료"),
    COMPLETED("사용 완료"),
    CANCELLED("취소 완료");

    private final String description;
}
