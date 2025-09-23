package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.*;
import com.ceos22.cgv_clone.dto.ticket.*;
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
    private final SeatRepository seatRepository;

    public ShowtimeSeatResponseDto getSeatsByShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));

        Screen screen = showtime.getScreen();
        List<Seat> seats = seatRepository.findByScreenId(screen.getId());

        // 예약된 seatId 가져오기
        Set<Long> reservedSeatIds = reservationSeatRepository
                .findByShowtimeIdAndStatus(showtimeId, ReservationStatus.RESERVED)
                .stream()
                .map(rs -> rs.getSeat().getId())
                .collect(Collectors.toSet());

        // 전체 좌석 리스트에 상태 반영
        List<SeatStatusDto> seatStatusList = seats.stream()
                .map(seat -> SeatStatusDto.builder()
                        .seatId(seat.getId())
                        .row(seat.getSeat_row())
                        .col(seat.getSeat_col())
                        .status(reservedSeatIds.contains(seat.getId()) ? "RESERVED" : "AVAILABLE")
                        .build())
                .toList();

        return ShowtimeSeatResponseDto.builder()
                .showtimeId(showtimeId)
                .screenName(screen.getName())
                .totalSeats(screen.getTotalSeats())
                .seats(seatStatusList)
                .build();
    }
}

