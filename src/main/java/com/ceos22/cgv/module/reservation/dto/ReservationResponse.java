package com.ceos22.cgv.module.reservation.dto;

import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.util.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReservationResponse(
        Long reservationId,
        Long scheduleId,
        String movieTitle,
        LocalDateTime createdAt,
        ReservationStatus status,
        List<SeatResponse> seats
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule().getId(),
                reservation.getSchedule().getMovie().getTitle(),
                reservation.getCreatedAt(),
                reservation.getStatus(),
                reservation.getReservationSeats().stream()
                        .map(s -> new SeatResponse(s.getRow(), s.getColumn()))
                        .toList()
        );
    }

    public record SeatResponse(Integer row, Integer column) {}
}
