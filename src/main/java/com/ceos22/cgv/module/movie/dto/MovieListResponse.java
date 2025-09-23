package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;

import java.util.List;

public record MovieListResponse(List<MovieResponse> movies) {
    public static MovieListResponse fromEntities(List<Movie> movies) {
        var list = movies.stream().map(MovieResponse::from).toList();
        return new MovieListResponse(list);
    }
}