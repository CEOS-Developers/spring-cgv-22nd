package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.response.SeatResponseDto;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

    private final ReservationSeatRepository reservationSeatRepository;
    private final ShowtimeRepository showtimeRepository;

    // 특정 상영 시간표의 예약 좌석 조회
    @Transactional(readOnly = true)
    public SeatResponseDto getSeatsByShowtime(Long showtimeId){
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.SHOWTIME_NOT_FOUND,
                        "showtimeId=%d를 찾을 수 없습니다.".formatted(showtimeId)));

        int totalRow = showtime.getScreen().getTotalRow();
        int totalCol = showtime.getScreen().getTotalCol();

        // 예약 좌석만 조회
        List<String> reservedSeats = reservationSeatRepository
                .findByShowtime_IdAndStatus(showtimeId, ReservationStatus.RESERVED)
                .stream()
                .map(rs -> rs.getSeatRow() + rs.getSeatCol())
                .toList();

        return SeatResponseDto.from(showtime, totalRow, totalCol, reservedSeats);

    }
}

