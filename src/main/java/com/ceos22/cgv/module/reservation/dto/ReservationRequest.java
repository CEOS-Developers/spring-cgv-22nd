package com.ceos22.cgv.module.reservation.dto;

import java.util.List;

public record ReservationRequest(
        Long scheduleId,
        List<SeatRequest> seats
) {
    public record SeatRequest(
            Integer row,
            Integer column) {
    }
}