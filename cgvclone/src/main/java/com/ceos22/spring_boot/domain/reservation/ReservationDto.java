package com.ceos22.spring_boot.domain.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationDto {

    @Getter
    @Builder
    public static class ReservationRequest {
        private Long screeningId;
        private List<Long> screeningSeatIds;
    }

    @Getter
    @Builder
    public static class ReservationResponse {
        private Long reservationId;
        private String movieTitle;
        private String screenName;
        private String theaterName;
        private List<String> seatNumbers;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Integer totalAmount;
        private String status;
    }
}

