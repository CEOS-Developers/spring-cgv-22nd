package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.BookedSeat;
import com.ceos22.springcgv.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {

    void deleteAllByBooking(Booking booking);
}