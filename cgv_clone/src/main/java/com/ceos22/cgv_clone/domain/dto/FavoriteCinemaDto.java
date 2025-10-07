package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinema;

public record FavoriteCinemaDto(
        Long id,
        Long cinemaId,
        String cinemaName
) {
    public static FavoriteCinemaDto from(FavoriteCinema fc) {
        return new FavoriteCinemaDto(
                fc.getId(),
                fc.getCinema().getId(),
                fc.getCinema().getName()
        );
    }
}
