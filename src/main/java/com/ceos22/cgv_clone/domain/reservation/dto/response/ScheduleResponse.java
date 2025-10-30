package com.ceos22.cgv_clone.domain.reservation.dto.response;

import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long scheduleId,
        String movieTitle,
        String theaterName,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getScheduleId(),
                schedule.getMovie().getTitle(),
                schedule.getScreen().getTheater().getName(),
                schedule.getScreen().getName(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}