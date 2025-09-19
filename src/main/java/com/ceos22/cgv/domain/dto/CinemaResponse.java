package com.ceos22.cgv.domain.dto;

import com.ceos22.cgv.domain.repository.Cinema;

public record CinemaResponse(Long id, String name, String region, String location) {
    public static CinemaResponse from(Cinema c) {
        return new CinemaResponse(c.getId(), c.getName(), c.getRegion(), c.getLocation());
    }
}
