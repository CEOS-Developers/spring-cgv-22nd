package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.cinema.domain.Theater;
import com.ceos22.cgv.module.movie.domain.Schedule;
import com.ceos22.cgv.util.TheaterType;

import java.time.LocalDateTime;

public record ScheduleResponse(Long scheduleId, Long theaterId, String theaterName, TheaterType theaterType, LocalDateTime startAt, LocalDateTime endAt, Integer seatAvailable, Integer totalSeat) {

    public static ScheduleResponse from(Schedule schedule, int seatAvailable, int totalSeat) {
        Theater t = schedule.getTheater();
        return new ScheduleResponse(
                schedule.getId(),
                t.getId(),
                t.getName(),
                t.getType(),
                schedule.getStartAt(),
                schedule.getEndAt(),
                seatAvailable,
                totalSeat
        );
    }

}
