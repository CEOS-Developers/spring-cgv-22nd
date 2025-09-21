package com.ceos22.cgv.module.cinema.dto;

import com.ceos22.cgv.module.cinema.domain.Cinema;

import java.util.List;

public record CinemaListResponse(List<CinemaResponse> cinemas) {
    public static CinemaListResponse fromEntities(List<Cinema> cinemas) {
        var list = cinemas.stream().map(CinemaResponse::from).toList();
        return new CinemaListResponse(list);
    }
}
