package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.AgeGroup;

public record TicketLine(Long ticketId, Long seatId, AgeGroup ageGroup, int unitPrice) {

}