package com.ceos22.cgv_clone.web.domain.reservation;



public class ReservationTotalPrice {

    private final int value;

    private ReservationTotalPrice(int value) {
        this.value = value;
    }

    public static ReservationTotalPrice of(int value) {
        return new ReservationTotalPrice(value);
    }

    public void validateNonNegative() {
        if (value < 0) {
            throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
        }
    }

    public int getValue() {
        return value;
    }
}
