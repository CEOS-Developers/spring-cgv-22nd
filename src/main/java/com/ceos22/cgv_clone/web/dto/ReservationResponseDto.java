package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReservationResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
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
