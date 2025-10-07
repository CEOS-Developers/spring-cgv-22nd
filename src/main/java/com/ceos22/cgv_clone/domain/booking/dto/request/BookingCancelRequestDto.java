package com.ceos22.cgv_clone.domain.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCancelRequestDto {

    private Long bookingId;
}
