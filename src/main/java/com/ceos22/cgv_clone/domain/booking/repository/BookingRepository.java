package com.ceos22.cgv_clone.domain.booking.repository;

import com.ceos22.cgv_clone.domain.booking.eneity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
           select b
             from Booking b
             join fetch b.member
             join fetch b.screening s
             join fetch s.auditorium a
             join fetch s.movie m
            where b.id = :id
           """)
    Optional<Booking> findDetailById(@Param("id") Long id);
}
