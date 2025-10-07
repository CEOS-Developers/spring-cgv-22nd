package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.user.domain.User;

public record MovieLikeResponse(Long movieId, Long userId, boolean isLiked) {
    public static MovieLikeResponse of(Movie movie, User user, boolean isLiked) {
        return new MovieLikeResponse(movie.getId(), user.getId(), isLiked);
    }
}
