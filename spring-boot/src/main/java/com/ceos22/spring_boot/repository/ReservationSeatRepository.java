package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.Reservation;
import com.ceos22.spring_boot.entity.mapping.ReservationSeat;
import com.ceos22.spring_boot.entity.mapping.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsByScreeningSeat(ScreeningSeat screeningSeat);
    List<ReservationSeat> findByReservation(Reservation reservation);
}
