package com.ceos22.cgv_clone.domain.theater.dto.response;

import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class ShowtimeResponseDto {
    private Long showtimeId;
    private LocalDate date;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 엔티티 -> Dto
    public static ShowtimeResponseDto fromEntity(Showtime showtime){
        return ShowtimeResponseDto.builder()
                .showtimeId(showtime.getId())
                .date(showtime.getDate())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .build();
    }
}
