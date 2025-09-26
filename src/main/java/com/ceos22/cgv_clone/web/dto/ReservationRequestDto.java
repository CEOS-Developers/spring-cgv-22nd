package com.ceos22.cgv_clone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReservationRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationDto{
        private Long userId;
        private Long scheduleId;
        private Integer adultAmount;
        private Integer teenAmount;
        private List<Long> seatIdList;
    }
}
