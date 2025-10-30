package com.ceos22.cgv.module.cinema.dto;

import com.ceos22.cgv.module.cinema.domain.Cinema;

import java.util.List;

public record CinemaListResponse(
        List<CinemaResponse> cinemas,
        int count
) {

    public static CinemaListResponse fromCinemas(
            List<Cinema> cinemas
    ) {
        var list = cinemas.stream().map(CinemaResponse::fromCinema).toList();
        return new CinemaListResponse(
                list,
                list.size()
        );
    }
}
