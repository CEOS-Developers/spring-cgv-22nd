package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ReservedSeatDto {
    private Long seatId;
    private String row;
    private String col;
}
