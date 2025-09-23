package com.ceos22.cgv_clone.dto.movie;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ShowtimeResponseDto {
    private Long showtimeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String screenName;
    private String screenType;
    private int totalSeats;
}
