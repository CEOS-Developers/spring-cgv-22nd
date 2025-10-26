package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinemaEntity;

public record FavoriteCinema(
        Long id,
        Long cinemaId
) {
    public static FavoriteCinema from(FavoriteCinemaEntity fc) {
        return new FavoriteCinema(
                fc.getId(),
                fc.getCinemaId()
        );
    }
}
