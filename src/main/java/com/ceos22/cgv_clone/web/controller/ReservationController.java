package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationResponseDto;
import com.ceos22.cgv_clone.web.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reservations")
    @Operation(summary = "예매하기 API", description = "영화를 예매하는 API입니다.")
    public ApiResponse<ReservationResponseDto.ReservationDto> createReservation(@RequestBody ReservationRequestDto.ReservationDto request) {
        return ApiResponse.onSuccess(reservationService.createReservation(request));
    }

    @DeleteMapping("/reservations/{reservationId}")
    @Operation(summary = "예매 취소하기 API", description = "예매를 취소하는 API입니다.")
    public ApiResponse<String> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ApiResponse.onSuccess("예매가 취소되었습니다.");
    }

}
