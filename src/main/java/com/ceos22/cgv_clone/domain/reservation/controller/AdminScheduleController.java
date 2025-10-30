package com.ceos22.cgv_clone.domain.reservation.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.request.ScheduleCreateRequest;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ScheduleResponse;
import com.ceos22.cgv_clone.domain.reservation.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
public class AdminScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ScheduleResponse>> createSchedule(@Valid @RequestBody ScheduleCreateRequest request) {
        ScheduleResponse scheduleResponse = scheduleService.createSchedule(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, scheduleResponse);
    }
}
