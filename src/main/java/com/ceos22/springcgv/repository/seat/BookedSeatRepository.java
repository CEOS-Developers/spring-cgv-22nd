package com.ceos22.springcgv.repository.seat;

import com.ceos22.springcgv.domain.booking.BookedSeat;
import com.ceos22.springcgv.domain.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    void deleteAllByBooking(Booking booking);
}