package com.ceos22.cgv_clone.web.domain.reservation;

import jakarta.persistence.Column;

public record ReservationAmounts(@Column(name = "adult_amount", nullable = false) int adultAmount,
                                 @Column(name = "teen_amount", nullable = false) int teenAmount) {

    public static ReservationAmounts of(int adultAmount, int teenAmount) {
        return new ReservationAmounts(adultAmount, teenAmount);
    }

    public void validatePositive() {
        if (adultAmount < 0 || teenAmount < 0) {
            throw new IllegalArgumentException("성인/청소년 수는 0 이상이어야 합니다.");
        }
    }

    public int getTotalAmount() {
        return adultAmount + teenAmount;
    }

    @Override
    public int adultAmount() {
        return adultAmount;
    }

    @Override
    public int teenAmount() {
        return teenAmount;
    }
}
