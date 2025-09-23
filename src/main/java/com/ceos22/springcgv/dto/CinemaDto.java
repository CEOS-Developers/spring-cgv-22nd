package com.ceos22.springcgv.dto;

import com.ceos22.springcgv.domain.Cinema;
import lombok.Getter;

@Getter
public class CinemaDto {
    private Long cinemaId;
    private String name;
    private String address;

    public CinemaDto(Cinema cinema) {
        this.cinemaId = cinema.getId();
        this.name = cinema.getName();
        this.address = cinema.getAddress();
    }
}