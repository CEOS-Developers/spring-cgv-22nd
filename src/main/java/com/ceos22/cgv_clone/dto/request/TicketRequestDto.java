package com.ceos22.cgv_clone.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TicketRequestDto {
    private Long showtimeId;
    private int generalCount;
    private int youthCount;
    private List<String> seatNumbers;
}
