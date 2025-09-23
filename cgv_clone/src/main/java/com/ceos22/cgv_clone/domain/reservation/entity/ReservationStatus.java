package com.ceos22.cgv_clone.domain.reservation.entity;

public enum ReservationStatus {
    CONFIRMED("예매 완료"),
    CANCELED("예매 취소"),
    COMPLETED("관람 완료");

    private final String status;

    ReservationStatus(String status) {
        this.status = status;
    }

    public String Status() {
        return status;
    }
}
