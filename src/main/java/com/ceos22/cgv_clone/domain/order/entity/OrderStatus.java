package com.ceos22.cgv_clone.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    CART("장바구니"),
    PENDING("결제 대기"),
    CONFIRMED("주문 완료"),
    CANCELLED("주문 취소"),
    COMPLETED("사용 완료");

    private final String description;
}
