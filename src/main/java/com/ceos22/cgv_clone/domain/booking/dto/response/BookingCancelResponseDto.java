package com.ceos22.cgv_clone.domain.booking.dto.response;

import com.ceos22.cgv_clone.domain.common.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCancelResponseDto {

    private Long bookingId;
    private BookingStatus status;   // CANCELED
    private LocalDateTime canceledAt;
}
