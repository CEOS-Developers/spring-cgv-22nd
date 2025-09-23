package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.util.Genre;
import com.ceos22.cgv.util.Rating;

import java.time.LocalDateTime;

public record MovieResponse(Long id, String title, Genre genre, Integer runtime, Rating rating, LocalDateTime releaseDate) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getGenre(), movie.getRuntime(), movie.getRating(), movie.getReleaseDate());
    }
}
