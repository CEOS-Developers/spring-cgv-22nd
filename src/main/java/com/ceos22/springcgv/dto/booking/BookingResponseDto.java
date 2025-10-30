package com.ceos22.springcgv.dto.booking;

import com.ceos22.springcgv.domain.booking.Booking;
import com.ceos22.springcgv.domain.enums.BookingStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class BookingResponseDto {
    private Long bookingId;
    private String movieTitle;
    private LocalDateTime showingStartTime;
    private BookingStatus status;
    private BigDecimal totalPrice;

    public BookingResponseDto(Booking booking) {
        this.bookingId = booking.getId();
        this.movieTitle = booking.getShowing().getMovie().getTitle();
        this.showingStartTime = booking.getShowing().getStartTime();
        this.status = booking.getStatus();
        this.totalPrice = booking.getTotalPrice();
    }
}