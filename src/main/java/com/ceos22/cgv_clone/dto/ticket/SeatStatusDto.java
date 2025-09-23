package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class SeatStatusDto {
    private Long seatId;
    private String row;
    private String col;
    private String status; // AVAILABLE or RESERVED
}
