package com.ceos22.cgv_clone.domain.movie.dto.response;

import com.ceos22.cgv_clone.domain.movie.entity.Movie;

public record MovieSimpleResponse(
        Long movieId,
        String title,
        String posterUrl
) {
    public static MovieSimpleResponse from(Movie movie) {
        return new MovieSimpleResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getPosterUrl()
        );
    }
}
