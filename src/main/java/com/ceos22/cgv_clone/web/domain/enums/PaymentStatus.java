package com.ceos22.cgv_clone.web.domain.enums;

public enum PaymentStatus {
    PENDING("결제 대기중"),
    PAID("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("결제 취소");

    private String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
