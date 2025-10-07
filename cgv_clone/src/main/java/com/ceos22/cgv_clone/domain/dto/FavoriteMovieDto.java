package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovie;

public record FavoriteMovieDto(
        Long id,
        Long movieId,
        String movieTitle
) {
    public static FavoriteMovieDto from(FavoriteMovie fm) {
        return new FavoriteMovieDto(
                fm.getId(),
                fm.getMovie().getId(),
                fm.getMovie().getMovieTitle()
        );
    }
}