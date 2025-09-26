package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Theater;
import com.ceos22.cgv_clone.web.domain.enums.TheaterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class TheaterResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TheaterDto {
        private Long theaterId;
        private TheaterType theaterType;
        private String name;
        private int maxSeats;
        private List<ScheduleResponseDto.ScheduleDto> schedules;

        public static TheaterDto of(Theater theater, List<ScheduleResponseDto.ScheduleDto> schedules) {
            int maxSeats = theater.getTotalCol() * theater.getTotalRow();
            return new TheaterDto(
                    theater.getId(),
                    theater.getTheaterType(),
                    theater.getName(),
                    maxSeats,
                    schedules
            );
        }
    }
}
