package com.ceos22.cgv_clone.domain.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatStatusDto {

    private Long seatId;
    private Integer rowNo;
    private Integer columnNo;
    private boolean reserved;
}
