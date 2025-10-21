package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.common.util.Genre;
import com.ceos22.cgv.common.util.Rating;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MovieResponse(Long id, String title, String director, Genre genre, Integer runtime, Rating rating, LocalDate releaseDate, String description, Integer totalAudience) {

    public static MovieResponse fromMovie(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getGenre(),
                movie.getRuntime(), movie.getRating(), movie.getReleaseDate(), movie.getDescription(), movie.getTotalAudience());
    }

    public static MovieResponse fromMovieToMovieSummary(Movie movie){
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}

