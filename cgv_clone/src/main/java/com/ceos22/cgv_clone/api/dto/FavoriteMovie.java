package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;

public record FavoriteMovie(
        Long id,
        Long movieId,
        Long fmMovieId
) {
    public static FavoriteMovie from(FavoriteMovieEntity fm) {
        return new FavoriteMovie(
                fm.getId(),
                fm.getMemberId(),
                fm.getMovieId()
        );
    }
}