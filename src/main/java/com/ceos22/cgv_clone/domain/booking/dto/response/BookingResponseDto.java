package com.ceos22.cgv_clone.domain.booking.dto.response;

import com.ceos22.cgv_clone.domain.booking.entity.Booking;
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
public class BookingResponseDto {

    // 파라미터 개수가 많아 ResponseDto에서도 빌더 패턴 적용

    private Long bookingId;
    private Long screeningId;
    private int adultCount;
    private int teenCount;
    private int totalPeople;
    private int totalPrice;
    private PaymentType paymentType;
    private BookingStatus status;
    private LocalDateTime bookingAt;
    private List<String> seats;

    public static BookingResponseDto of(Booking booking, List<String> seatLabels) {
        return BookingResponseDto.builder()
                .bookingId(booking.getId())
                .screeningId(booking.getScreening().getId())
                .adultCount(booking.getAdultCount())
                .teenCount(booking.getTeenCount())
                .totalPeople(booking.getTotalPeople())
                .totalPrice(booking.getTotalPrice())
                .paymentType(booking.getPaymentType())
                .status(booking.getStatus())
                .bookingAt(booking.getBookingAt())
                .seats(seatLabels)
                .build();
    }
}
