package com.ceos22.cgv_clone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReservationResponseDto {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationDto {
        private Long id;
        private Long scheduleId;
        private Integer totalAmount;
        private Integer totalPrice;
        private List<String> reservedSeatNames;
    }
}
