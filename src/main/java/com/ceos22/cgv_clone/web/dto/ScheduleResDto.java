package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleResDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleDto {
        private Long scheduleId;
        private String startTime;
        private String endTime;
        private MovieResDto.MovieDto movie;

        public static ScheduleDto of(Schedule schedule) {
            return new ScheduleDto(
                    schedule.getId(),
                    schedule.getStartTime().toString(),
                    schedule.getEndTime().toString(),
                    MovieResDto.MovieDto.of(schedule.getMovie())
            );
        }
    }
}
