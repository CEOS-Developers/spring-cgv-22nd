package com.ceos22.spring_boot.domain.theater.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScreeningRequestDto (
        @NotNull Long movieId,
        @NotNull Long screenId,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        String language
) {}
