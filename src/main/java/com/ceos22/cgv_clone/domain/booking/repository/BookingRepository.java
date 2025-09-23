package com.ceos22.cgv_clone.domain.booking.repository;

import com.ceos22.cgv_clone.domain.booking.eneity.Booking;

import java.util.List;

public interface BookingRepository {

    List<Booking> findByScreeningId(Long screeningId);

    List<Booking> findByMemberIdOrderByBookingAtDesc(Long memberId);

    boolean existsByMemberIdAndScreeningId(Long memberId, Long screeningId);
}
