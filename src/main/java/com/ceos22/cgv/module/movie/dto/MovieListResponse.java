package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;

import java.util.List;

public record MovieListResponse(List<MovieResponse> movies) {

    public static MovieListResponse fromMovies(List<Movie> movies) {
        var list = movies.stream().map(MovieResponse::fromMovie).toList();
        return new MovieListResponse(list);
    }

    public static MovieListResponse fromMovieSummaries(List<Movie> movies) {
        var list = movies.stream().map(MovieResponse::fromMovieToMovieSummary).toList();
        return new MovieListResponse(list);
    }
}