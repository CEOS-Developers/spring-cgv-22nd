package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.reservation.ReservationStatus;
import com.ceos22.cgv_clone.domain.theater.Screen;
import com.ceos22.cgv_clone.domain.theater.Showtime;
import com.ceos22.cgv_clone.dto.response.SeatResponseDto;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final ReservationSeatRepository reservationSeatRepository;
    private final ShowtimeRepository showtimeRepository;

    // 특정 상영 시간표의 예약 좌석 조회
    public SeatResponseDto getSeatsByShowtime(Long showtimeId){

        // 상영시간표 예외 처리
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(()->new IllegalArgumentException("상영시간표 없음"));

        int totalRow = showtime.getScreen().getTotalRow();
        int totalCol = showtime.getScreen().getTotalCol();

        List <String> reservedSeats = reservationSeatRepository
                .findByShowtime_IdAndStatus(showtimeId, ReservationStatus.RESERVED)
                .stream()
                .map(rs -> rs.getSeatRow() + rs.getSeatCol())
                .toList();

        return SeatResponseDto.from(showtime, totalRow, totalCol, reservedSeats);
    }
}

