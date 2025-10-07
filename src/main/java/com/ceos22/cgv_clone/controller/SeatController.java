package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.response.SeatResponseDto;
import com.ceos22.cgv_clone.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // 특정 상영시간표의 예약 좌석 조회
    @GetMapping("/{showtimeId}/seats")
    public SeatResponseDto getSeats(@PathVariable Long showtimeId) {
        return seatService.getSeatsByShowtime(showtimeId);
    }
}
