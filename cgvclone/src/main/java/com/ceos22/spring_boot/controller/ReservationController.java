package com.ceos22.spring_boot.controller;

import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;
import com.ceos22.spring_boot.dto.ReservationDto;
import com.ceos22.spring_boot.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation API", description = "예약 API")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예매 생성", description = "새로운 영화 예매를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDto.ReservationResponse>> createReservation(
            @RequestBody ReservationDto.ReservationRequest request
    ) {
        ReservationDto.ReservationResponse response = reservationService.createReservation(request);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }


    @Operation(summary = "예매 조회", description = "예매 ID를 사용해 특정 예매 정보를 조회합니다.")
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDto.ReservationResponse>> getReservation(
            @PathVariable Long reservationId
    ) {
        ReservationDto.ReservationResponse response = reservationService.getReservation(reservationId);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    @Operation(summary = "예매 취소", description = "예매 ID를 사용해 특정 예매를 취소합니다.")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<Object>> cancelReservation(
            @PathVariable Long reservationId
    ) {
        reservationService.cancelReservation(reservationId);
        return ApiResponse.onSuccess(SuccessStatus._OK, null);
    }
}
