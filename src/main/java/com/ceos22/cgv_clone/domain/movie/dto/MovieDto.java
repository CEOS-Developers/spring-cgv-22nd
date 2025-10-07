package com.ceos22.cgv_clone.domain.movie.dto;

import com.ceos22.cgv_clone.domain.common.enums.Genre;
import com.ceos22.cgv_clone.domain.common.enums.Rating;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MovieDto {

    private final Long movieId;
    private final String title;
    private final LocalDate releaseDate;
    private final Integer runtime;
    private final Genre genre;
    private final Rating rating;
    private final String posterUrl;

    public static MovieDto of(Movie movie) {
        return new MovieDto(
                movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getRuntime(), movie.getGenre(), movie.getRating(), movie.getPosterUrl()
        );
    }
}
