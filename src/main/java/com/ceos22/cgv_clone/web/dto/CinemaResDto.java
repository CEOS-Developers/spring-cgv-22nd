package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Cinema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CinemaResDto {

    @Getter
    @AllArgsConstructor
    public static class CinemaDto{
        private Long cinemaId;
        private String cinemaName;
        private String cinemaAddress;

        public static CinemaDto of(Cinema cinema){
            return new CinemaDto(
                    cinema.getId(),
                    cinema.getName(),
                    cinema.getAddress()
            );
        }
    }

}
