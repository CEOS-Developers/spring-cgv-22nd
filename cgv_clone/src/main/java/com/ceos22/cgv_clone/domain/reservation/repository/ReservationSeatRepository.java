package com.ceos22.cgv_clone.domain.reservation.repository;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    @Query("SELECT rs FROM ReservationSeat rs WHERE rs.reservation.schedule.scheduleId = :scheduleId")
    List<ReservationSeat> findAllByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query("SELECT COUNT(rs) > 0 FROM ReservationSeat rs " +
            "WHERE rs.reservation.schedule.scheduleId = :scheduleId "+
            "AND rs.seat.seatId IN :seatIds")
    boolean existsByScheduleIdAndSeatIds(@Param("scheduleId") Long scheduleId, @Param("seatIds") List<Long> seatIds);
}
