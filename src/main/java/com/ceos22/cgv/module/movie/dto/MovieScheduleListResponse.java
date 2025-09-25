package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.module.movie.domain.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record MovieScheduleListResponse(List<MovieScheduleResponse> movies) {

    public static MovieScheduleListResponse fromGrouped(Map<Movie, List<ScheduleResponse>> grouped) {
        List<MovieScheduleResponse> movieScheduleList = grouped.entrySet().stream()
                .map(e -> MovieScheduleResponse.of(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(MovieScheduleResponse::movieTitle))
                .toList();
        return new MovieScheduleListResponse(movieScheduleList);
    }

}
