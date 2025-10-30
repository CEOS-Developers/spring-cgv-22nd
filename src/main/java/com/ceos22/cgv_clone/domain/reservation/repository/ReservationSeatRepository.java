package com.ceos22.cgv_clone.domain.reservation.repository;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    // 특정 상영 시간표의 예약 좌석 확인
    List<ReservationSeat> findByShowtime_IdAndStatus(Long showtimeId, ReservationStatus status);


    // 특정 상영 시간표의 좌석 상태 확인
    boolean existsByShowtime_IdAndSeatRowAndSeatColAndStatus(Long showtimeId, String seatRow, String seatCol, ReservationStatus status);

    // 특정 티켓의 예약 좌석 삭제
    void deleteByTicket_Id(Long ticketId);
}
