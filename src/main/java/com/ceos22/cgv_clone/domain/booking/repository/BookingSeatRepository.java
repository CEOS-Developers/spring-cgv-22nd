package com.ceos22.cgv_clone.domain.booking.repository;

import com.ceos22.cgv_clone.domain.booking.entity.Booking;
import com.ceos22.cgv_clone.domain.booking.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    @Query("""
        SELECT bs FROM BookingSeat bs 
        JOIN FETCH bs.seat 
        WHERE bs.booking = :booking
    """)
    List<BookingSeat> findByBookingWithSeat(@Param("booking") Booking booking);


    void deleteByBooking(Booking booking);

    // 좌석 현황 조회용
    List<BookingSeat> findByScreeningId(Long screeningId);
}
