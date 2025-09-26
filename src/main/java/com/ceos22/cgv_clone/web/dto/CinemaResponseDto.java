package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Cinema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class CinemaResponseDto {

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

    @Getter
    @AllArgsConstructor
    public static class CinemaDetailDto{
        private Long cinemaId;
        private String cinemaName;
        private String cinemaAddress;
        private List<TheaterResponseDto.TheaterDto> theaterList;

        public static CinemaDetailDto of(Cinema cinema, List<TheaterResponseDto.TheaterDto> theaters){
            return new CinemaDetailDto(
                    cinema.getId(),
                    cinema.getName(),
                    cinema.getAddress(),
                    theaters
            );
        }
    }

}
