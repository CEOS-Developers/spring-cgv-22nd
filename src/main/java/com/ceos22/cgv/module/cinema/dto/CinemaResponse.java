package com.ceos22.cgv.module.cinema.dto;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.common.util.Region;

public record CinemaResponse(
        Long id,
        String name,
        Region region,
        String location,
        String description
) {

    public static CinemaResponse fromCinema(Cinema cinema) {
        return new CinemaResponse(
                cinema.getId(),
                cinema.getName(),
                cinema.getRegion(),
                cinema.getLocation(),
                cinema.getDescription()
        );
    }
}