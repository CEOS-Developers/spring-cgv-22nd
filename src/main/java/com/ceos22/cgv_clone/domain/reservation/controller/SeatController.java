package com.ceos22.cgv_clone.domain.reservation.controller;

import com.ceos22.cgv_clone.domain.reservation.dto.response.SeatResponseDto;
import com.ceos22.cgv_clone.domain.reservation.service.SeatService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // 특정 상영시간표의 예약 좌석 조회
    @GetMapping("/showtimes/{showtimeId}/seats")
    public ApiResponse<SeatResponseDto> getSeats(@PathVariable Long showtimeId) {
        SeatResponseDto seatResponse = seatService.getSeatsByShowtime(showtimeId);
        return ApiResponse.of(seatResponse, SuccessCode.GET_SUCCESS);
    }
}
