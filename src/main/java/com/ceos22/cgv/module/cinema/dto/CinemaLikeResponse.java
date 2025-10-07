package com.ceos22.cgv.module.cinema.dto;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.user.domain.User;

public record CinemaLikeResponse(Long cinemaId, Long userId, boolean isLiked) {
    public static CinemaLikeResponse of(Cinema cinema, User user, boolean isLiked) {
        return new CinemaLikeResponse(cinema.getId(), user.getId(), isLiked);
    }
}