package com.ceos22.cgv_clone.domain.booking.dto.response;
import com.ceos22.cgv_clone.domain.common.enums.BookingStatus;
import com.ceos22.cgv_clone.domain.common.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponseDto {

    private Long bookingId;

    // 헤더
    private Long memberId;
    private PaymentType paymentType;
    private BookingStatus status;
    private LocalDateTime bookingAt;
    private Integer adultCount;
    private Integer teenCount;
    private Integer totalPeople;
    private Integer totalPrice;

    // 상영회차 정보 (표시용)
    private Long screeningId;
    private String movieTitle;
    private String auditoriumName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    // 좌석
    private List<String> seats;
}
