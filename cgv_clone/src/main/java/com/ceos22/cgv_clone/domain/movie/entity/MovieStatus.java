package com.ceos22.cgv_clone.domain.movie.entity;

public enum MovieStatus {
    NOW_PLAYING("상영중"),
    COMING_SOON("개봉예정"),
    SCREENING_ENDED("상영종료");

    private final String movieStatus;

    MovieStatus(String movieStatus) {
        this.movieStatus = movieStatus;
    }

    public String getMovieStatus() {
        return movieStatus;
    }
}
