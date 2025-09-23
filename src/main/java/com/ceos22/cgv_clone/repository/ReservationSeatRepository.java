package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    // 상영 시간 + 좌석의 예약 여부 확인
    boolean existsByShowtime_IdAndSeat_IdAndStatus(Long showtimeId, Long seatId, ReservationStatus status);

    // 티켓과 연결된 좌석 찾기
    List<ReservationSeat> findByTicket(Ticket ticket);

    // 특정 상영 시간표의 예약 좌석 확인
    List<ReservationSeat> findByShowtimeIdAndStatus(Long showtimeId, ReservationStatus status);

    // 특정 티켓의 좌석 삭제
    void deleteByTicket(Ticket ticket);
}
