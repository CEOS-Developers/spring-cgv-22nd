package com.ceos22.cgv_clone.domain.screening.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScreeningDto {
    private Long screeningId;
    private Long movieId;
    private String movieTitle;
    private Long auditoriumId;
    private String auditoriumName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
