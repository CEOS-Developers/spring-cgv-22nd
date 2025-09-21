package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Theater;
import com.ceos22.cgv_clone.web.domain.enums.TheaterType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class TheaterResDto {

    @Getter
    @AllArgsConstructor
    public static class TheaterDto {
        private Long theaterId;
        private TheaterType theaterType;
        private String name;
        private int maxSeats;
        private List<ScheduleResDto.ScheduleDto> schedules;

        public static TheaterDto of(Theater theater, List<ScheduleResDto.ScheduleDto> schedules) {
            return new TheaterDto(
                    theater.getId(),
                    theater.getTheaterType(),
                    theater.getName(),
                    theater.getMaxSeats(),
                    schedules
            );
        }
    }
}
