package com.ceos22.cgv.module.reservation.dto;

import java.util.List;

public record SeatAvailabilityResponse(
        Long scheduleId,
        int numRow,
        int numColumn,
        List<Seat> seats
) {
    public record Seat(int row, int column, boolean booked) {}
}