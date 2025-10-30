package com.ceos22.springcgv.dto.cinema;

import com.ceos22.springcgv.domain.cinema.Cinema;
import lombok.Getter;

@Getter
public class CinemaDto {
    private final Long cinemaId;
    private final String name;
    private final String address;
    private final Boolean isLiked;

    public CinemaDto(Cinema cinema) {
        this.cinemaId = cinema.getId();
        this.name = cinema.getName();
        this.address = cinema.getAddress();
        this.isLiked = false;
    }

    public CinemaDto(Cinema cinema, Boolean isLiked) {
        this.cinemaId = cinema.getId();
        this.name = cinema.getName();
        this.address = cinema.getAddress();
        this.isLiked = isLiked;
    }
}