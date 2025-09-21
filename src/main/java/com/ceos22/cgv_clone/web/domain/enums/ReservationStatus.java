package com.ceos22.cgv_clone.web.domain.enums;

public enum ReservationStatus {
    RESERVED("예매 완료"),
    CANCELLED("예매 취소");

    private String display;

    ReservationStatus(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
