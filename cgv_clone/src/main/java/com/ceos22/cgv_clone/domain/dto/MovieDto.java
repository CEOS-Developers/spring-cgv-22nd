package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.reservationMovie.Movie;

public record MovieDto(
        Long id,
        String movieTitle,
        Integer runningTime,
        String introduction
) {
    public static MovieDto from(Movie m) {
        return new MovieDto(
                m.getId(),
                m.getMovieTitle(),
                m.getRunningTime(),
                m.getIntroduction()
        );
    }
}