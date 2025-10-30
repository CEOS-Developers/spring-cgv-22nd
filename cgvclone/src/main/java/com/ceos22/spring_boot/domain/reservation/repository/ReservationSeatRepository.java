package com.ceos22.spring_boot.domain.reservation.repository;

import com.ceos22.spring_boot.domain.reservation.entity.Reservation;
import com.ceos22.spring_boot.domain.reservation.entity.ReservationSeat;
import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    List<ReservationSeat> findByReservation(Reservation reservation);

    @Query("SELECT rs.screeningSeat.ssId FROM ReservationSeat rs WHERE rs.screeningSeat IN :seats")
    List<Long> findReservedSeatIds(@Param("seats") List<ScreeningSeat> seats);

}
