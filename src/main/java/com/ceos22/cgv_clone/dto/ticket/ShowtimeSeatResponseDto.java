package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ShowtimeSeatResponseDto {
    private Long showtimeId;
    private String screenName;
    private int totalSeats;
    private java.util.List<SeatStatusDto> seats;
}
