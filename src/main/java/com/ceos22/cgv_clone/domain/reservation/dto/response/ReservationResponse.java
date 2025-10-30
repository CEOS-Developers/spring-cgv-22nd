package com.ceos22.cgv_clone.domain.reservation.dto.response;

import com.ceos22.cgv_clone.domain.reservation.entity.Reservation;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(
        Long reservationId,
        String movieTitle,
        String posterUrl,
        String theaterName,
        String screenName,
        ScreenType screenType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer totalPrice,
        List<SeatInfo> reservedSeats,
        ReservationStatus status
) {
    public record SeatInfo(
            String rowName,
            String columnNumber
    ) {
        public static SeatInfo from(ReservationSeat reservationSeat) {
            return new SeatInfo(
                    reservationSeat.getSeat().getRowName(),
                    reservationSeat.getSeat().getColumnNumber()
            );
        }
    }

    public static ReservationResponse from(Reservation reservation) {
        List<SeatInfo> seatInfos = reservation.getReservationSeats().stream()
                .map(SeatInfo::from)
                .toList();

        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getSchedule().getMovie().getTitle(),
                reservation.getSchedule().getMovie().getPosterUrl(),
                reservation.getSchedule().getScreen().getTheater().getName(),
                reservation.getSchedule().getScreen().getName(),
                reservation.getSchedule().getScreen().getType(),
                reservation.getSchedule().getStartTime(),
                reservation.getSchedule().getEndTime(),
                reservation.getTotalPrice(),
                seatInfos,
                reservation.getStatus()
        );
    }
}
