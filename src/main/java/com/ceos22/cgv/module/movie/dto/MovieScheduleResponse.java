package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.movie.domain.Schedule;

import java.util.Comparator;
import java.util.List;

public record MovieScheduleResponse(
        Long movieId,
        String movieTitle,
        List<ScheduleResponse> schedules
) {
    public static MovieScheduleResponse of(Movie movie, List<ScheduleResponse> schedules) {
        return new MovieScheduleResponse(
                movie.getId(),
                movie.getTitle(),
                schedules
        );
    }
}
