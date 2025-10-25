package com.ceos22.cgv_clone.domain.dto;

public record CreateFavoriteMovieCommand(
        Long id,
        String movieTitle
) {}
