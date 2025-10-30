package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.AgeGroup;

public record SeatSelection(Long seatId, AgeGroup ageGroup) {
}
