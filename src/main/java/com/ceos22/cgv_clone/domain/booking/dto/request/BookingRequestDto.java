package com.ceos22.cgv_clone.domain.booking.dto.request;

import com.ceos22.cgv_clone.domain.common.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {

    private Long screeningId;
    private List<Long> seatIds;

    private int adultCount;
    private int teenCount;
    private PaymentType paymentType;
}
