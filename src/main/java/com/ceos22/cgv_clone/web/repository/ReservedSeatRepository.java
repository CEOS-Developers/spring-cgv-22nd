package com.ceos22.cgv_clone.web.repository;

import com.ceos22.cgv_clone.web.domain.reservation.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {
    boolean existsByScheduleIdAndSeatIdInAndIsAvailableFalse(Long scheduleId, List<Long> seatIds);

    boolean existsByScheduleIdAndSeatIdAndIsAvailableFalse(Long scheduleId, Long seatId);
}
