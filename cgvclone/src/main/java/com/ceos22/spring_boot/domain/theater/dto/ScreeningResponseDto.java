package com.ceos22.spring_boot.domain.theater.dto;

import com.ceos22.spring_boot.domain.theater.entity.Screening;

import java.time.LocalDateTime;

public record ScreeningResponseDto(
        Long screeningId,
        String movieTitle,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String language
) {
    public static ScreeningResponseDto from(Screening screening) {
        return new ScreeningResponseDto(
                screening.getScreeningId(),
                screening.getMovie().getTitle(),
                screening.getScreen().getScreenName(),
                screening.getStartTime(),
                screening.getEndTime(),
                screening.getLanguage()
        );
    }
}