package com.ceos22.cgv.module.reservation.repository;

import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    @Query("""
        select count(rs) 
        from ReservationSeat rs 
        where rs.schedule.id = :scheduleId
    """)
    long countByScheduleId(Long scheduleId);

    // 선검증: 해당 좌표가 이미 BOOKED인지 단건 확인 (과거 로직)
    @Query("""
      select (count(rs) > 0) from ReservationSeat rs
      join rs.reservation r
      where rs.schedule.id = :scheduleId
        and rs.row = :row and rs.column = :col
        and r.status = com.ceos22.cgv.common.util.ReservationStatus.RESERVED
    """)
    boolean existsBooked(@Param("scheduleId") Long scheduleId,
                         @Param("row") Integer row,
                         @Param("col") Integer col);

    // 개선: 예약 완료 또는 만료되지 않은 HOLD를 모두 점유로 간주
    @Query("""
      select (count(rs) > 0) from ReservationSeat rs
      join rs.reservation r
      where rs.schedule.id = :scheduleId
        and rs.row = :row and rs.column = :col
        and (
          r.status = com.ceos22.cgv.common.util.ReservationStatus.RESERVED or
          r.status = com.ceos22.cgv.common.util.ReservationStatus.HOLD
        )
    """)
    boolean existsOccupied(@Param("scheduleId") Long scheduleId,
                           @Param("row") Integer row,
                           @Param("col") Integer col);
}
