package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.util.Genre;
import com.ceos22.cgv.util.Rating;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MovieResponse(Long id, String title, String director, Genre genre, Integer runtime, Rating rating, LocalDate releaseDate, String description, Integer totalAudience) {

    public static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getRuntime(), movie.getRating(), movie.getReleaseDate(), movie.getDescription(), movie.getTotalAudience());
    }
}

