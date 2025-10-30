package com.ceos22.cgv_clone.web.domain.reservation;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationUuid {

    @Column(nullable = false, updatable = false, name = "uuid_value")
    private String value;

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
