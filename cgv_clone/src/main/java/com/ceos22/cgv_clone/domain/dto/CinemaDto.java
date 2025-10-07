package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.Cinema;

public record CinemaDto(
        Long id,
        String name
) {
    public static CinemaDto from(Cinema c) {
        return new  CinemaDto(
                c.getId(),
                c.getName()
        );
    }
}
