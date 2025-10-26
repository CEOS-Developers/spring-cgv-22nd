package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.MovieEntity;

public record Movie(
        Long id,
        String movieTitle,
        Integer runningTime,
        String introduction
) {
    public static Movie from(MovieEntity m) {
        return new Movie(
                m.getId(),
                m.getMovieTitle(),
                m.getRunningTime(),
                m.getIntroduction()
        );
    }
}