package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.Reservation;
import com.ceos22.cgv_clone.domain.reservationMovie.ReservationStatus;

import java.util.List;

public record ReservationSummaryDto(
        Long reservationId,
        Long screeningId,
        int totalAmount,
        ReservationStatus status,
        List<TicketLine> tickets
) {
    public static ReservationSummaryDto from(Reservation r){
        return new ReservationSummaryDto(
                r.getId(),
                r.getScreening().getId(),
                r.getTotalAmount(),
                r.getStatus(),
                r.getTickets().stream()
                        .map(t -> new TicketLine(t.getId(), t.getSeat().getId(), t.getAgeGroup(), t.getUnitPrice()))
                        .toList()
        );
    }
}
