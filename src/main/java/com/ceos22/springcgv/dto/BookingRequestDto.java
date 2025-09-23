package com.ceos22.springcgv.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class BookingRequestDto {
    private Long showingId;
    private List<Long> seatIds;
}