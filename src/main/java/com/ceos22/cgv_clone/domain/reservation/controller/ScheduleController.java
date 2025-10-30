package com.ceos22.cgv_clone.domain.reservation.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ScheduleResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ScheduleSeatResponse;
import com.ceos22.cgv_clone.domain.reservation.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getSchedules(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long theaterId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<ScheduleResponse> schedules = scheduleService.findSchedules(movieId, theaterId, date);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, schedules);
    }

    @GetMapping("/{scheduleId}/seats")
    public ResponseEntity<ApiResponse<ScheduleSeatResponse>> getScheduleSeats(@PathVariable Long scheduleId) {
        ScheduleSeatResponse scheduleSeatResponse = scheduleService.findScheduleWithSeatStatus(scheduleId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, scheduleSeatResponse);
    }
}
