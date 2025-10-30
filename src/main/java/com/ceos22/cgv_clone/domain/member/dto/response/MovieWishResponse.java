package com.ceos22.cgv_clone.domain.member.dto.response;

import com.ceos22.cgv_clone.domain.member.entity.MovieWish;

import java.time.LocalDateTime;

public record MovieWishResponse(
        Long movieWishId,
        Long movieId,
        String movieTitle,
        String posterUrl,
        LocalDateTime createdAt
){
    public static MovieWishResponse from(MovieWish movieWish) {
        return new MovieWishResponse(
                movieWish.getMovieWishId(),
                movieWish.getMovie().getMovieId(),
                movieWish.getMovie().getTitle(),
                movieWish.getMovie().getPosterUrl(),
                movieWish.getCreatedAt()
        );
    }
}
