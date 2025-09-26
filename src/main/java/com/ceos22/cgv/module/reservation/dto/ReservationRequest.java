package com.ceos22.cgv.module.reservation.dto;

import java.util.List;

public record ReservationRequest(
        Long scheduleId,
        List<SeatReqest> seats
) {
    public record SeatReqest(Integer row, Integer column) {}
}