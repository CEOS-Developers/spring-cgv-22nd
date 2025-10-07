package com.ceos22.cgv_clone.domain.dto;

import java.util.List;

public record CreateReservationCommand(
        Long memberId,
        Long screeningId,
        List<SeatSelection> selections
) {}
