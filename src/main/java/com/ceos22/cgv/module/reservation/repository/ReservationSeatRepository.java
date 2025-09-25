package com.ceos22.cgv.module.reservation.repository;

import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {

    @Query("""
        select count(rs) 
        from ReservationSeat rs 
        where rs.schedule.id = :scheduleId
    """)
    int countByScheduleId(Long scheduleId);
}
