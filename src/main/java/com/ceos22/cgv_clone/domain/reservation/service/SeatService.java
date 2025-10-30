package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.response.SeatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

    private final ReservationSeatRepository reservationSeatRepository;
    private final ShowtimeRepository showtimeRepository;

    // 특정 상영 시간표의 예약 좌석 조회
    public SeatResponseDto getSeatsByShowtime(Long showtimeId){

        log.debug("[SVC] getSeatsByShowtime start - showtimeId={}", showtimeId);
        try {
            Showtime showtime = showtimeRepository.findById(showtimeId)
                    .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));

            int totalRow = showtime.getScreen().getTotalRow();
            int totalCol = showtime.getScreen().getTotalCol();

            List<String> reservedSeats = reservationSeatRepository
                    .findByShowtime_IdAndStatus(showtimeId, ReservationStatus.RESERVED)
                    .stream()
                    .map(rs -> rs.getSeatRow() + rs.getSeatCol())
                    .toList();

            log.info("[SVC] 좌석 조회 완료 - showtimeId={}, reserved={}석, size={}x{}",
                    showtimeId, reservedSeats.size(), totalRow, totalCol);

            return SeatResponseDto.from(showtime, totalRow, totalCol, reservedSeats);

        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(좌석 조회) - showtimeId={}, msg={}", showtimeId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 좌석 조회 실패 - showtimeId={}", showtimeId, e);
            throw e;
        }
    }
}

