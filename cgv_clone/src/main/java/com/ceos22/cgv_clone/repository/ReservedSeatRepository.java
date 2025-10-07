package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.reservationMovie.HoldStatus;
import com.ceos22.cgv_clone.domain.reservationMovie.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    @Query("""
      select count(rs) from ReservedSeat rs
      where rs.screening.id = :screeningId
        and rs.seat.id in :seatIds
        and rs.holdStatus in :statuses
    """)
    long countOccupied(@Param("screeningId") Long screeningId,
                       @Param("seatIds") Collection<Long> seatIds,
                       @Param("statuses") Collection<HoldStatus> statuses);

    @Query("""
      select rs from ReservedSeat rs
      where rs.reservation.id = :reservationId
    """)
    List<ReservedSeat> findAllByReservation(@Param("reservationId") Long reservationId);
}