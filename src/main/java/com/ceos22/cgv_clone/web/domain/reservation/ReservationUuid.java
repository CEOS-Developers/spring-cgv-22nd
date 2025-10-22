package com.ceos22.cgv_clone.web.domain.reservation;

import jakarta.persistence.Column;

import java.util.UUID;

public class ReservationUuid {

    @Column(nullable = false, updatable = false)
    private final String value;

    private ReservationUuid(String value) {
        this.value = value;
    }

    public static ReservationUuid generate() {
        return new ReservationUuid(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }
}
