package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class ReservationResponseDto {

    @Getter
    @AllArgsConstructor
    public static class ReservationDto {
        private Long id;
        private Long scheduleId;
        private Integer totalAmount;
        private Integer totalPrice;
        private List<String> reservedSeatNames;

        public static ReservationDto of(Reservation reservation, List<String> seats) {
            return new ReservationDto(
                    reservation.getId(),
                    reservation.getSchedule().getId(),
                    reservation.getReservationAmounts().getTotalAmount(),
                    reservation.getReservationTotalPrice().getValue(),
                    seats
            );
        }
    }
}
