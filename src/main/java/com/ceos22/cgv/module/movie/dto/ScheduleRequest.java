package com.ceos22.cgv.module.movie.dto;

import com.ceos22.cgv.common.util.ScheduleCategory;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ScheduleRequest(
        Long movieId,
        @NotNull Long cinemaId,
        @NotNull LocalDate date,
        ScheduleCategory category
    ) {
}
