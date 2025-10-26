package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;

public record Cinema(
        Long id,
        String name
) {
    public static Cinema from(CinemaEntity c) {
        return new Cinema(
                c.getId(),
                c.getName()
        );
    }
}
