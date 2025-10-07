package com.ceos22.spring_boot.domain.reservation.repository;

import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import com.ceos22.spring_boot.domain.reservation.entity.ReservationSeat;
import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsByScreeningSeat(ScreeningSeat screeningSeat);
    List<ReservationSeat> findByReservation(Reservation reservation);
}
