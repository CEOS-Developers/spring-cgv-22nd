package com.ceos22.cgv.module.reservation.repository;

import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
      select r from Reservation r
      join fetch r.schedule s
      where r.id = :id
    """)
    Optional<Reservation> findByIdWithSchedule(@Param("id") Long id);

    // BOOKED 상태의 좌석만 카운트/조회
    @Query("""
      select count(rs) from ReservationSeat rs
      join rs.reservation r
      where rs.schedule.id = :scheduleId
        and r.status = com.ceos22.cgv.common.util.ReservationStatus.RESERVED
    """)
    long countBooked(@Param("scheduleId") Long scheduleId);

    @Query("""
      select rs from ReservationSeat rs
      join rs.reservation r
      where rs.schedule.id = :scheduleId
        and r.status = com.ceos22.cgv.common.util.ReservationStatus.RESERVED
    """)
    List<ReservationSeat> findBookedSeats(@Param("scheduleId") Long scheduleId);


    @Query("""
      select rs from ReservationSeat rs
      join rs.reservation r
      where rs.schedule.id = :scheduleId
        and (
          r.status = com.ceos22.cgv.common.util.ReservationStatus.RESERVED or
          r.status = com.ceos22.cgv.common.util.ReservationStatus.HOLD
        )
    """)
    List<ReservationSeat> findOccupiedSeats(@Param("scheduleId") Long scheduleId);



    @Query("select distinct r from Reservation r " +
            "left join fetch r.schedule s " +
            "left join fetch s.movie m " +
            "left join fetch r.reservationSeats rs " +
            "where r.user.id = :userId")
    List<Reservation> findAllByUserIdWithScheduleAndSeats(@Param("userId") Long userId);

    @Query("select r from Reservation r " +
            "left join fetch r.schedule s " +
            "left join fetch s.movie m " +
            "left join fetch r.reservationSeats rs " +
            "where r.id = :reservationId")
    Optional<Reservation> findByIdWithScheduleAndSeats(@Param("reservationId") Long reservationId);
}
