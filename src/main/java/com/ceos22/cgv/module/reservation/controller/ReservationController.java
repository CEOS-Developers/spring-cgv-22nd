package com.ceos22.cgv.module.reservation.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.reservation.dto.ReservationRequest;
import com.ceos22.cgv.module.reservation.dto.ReservationResponse;
import com.ceos22.cgv.module.reservation.service.ReservationService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "좌석 예약", description = "로그인한 사용자가 선택한 상영 스케줄과 좌석 정보로 예약을 생성합니다. (인증 필요)")
    @PostMapping("/reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.of(
                reservationService.reserve(request, user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "예약 취소", description = "본인 소유의 예약을 취소합니다. (인증 필요)")
    @PostMapping("/reservations/{reservationId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> cancel(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.of(
                reservationService.cancel(reservationId,user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "나의 예약 목록", description = "로그인한 사용자의 모든 예약 내역을 조회합니다. (인증 필요)")
    @GetMapping("/reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> myReservations(
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<List<ReservationResponse>> response = ApiResponse.of(
                reservationService.getMyReservations(user.getUser()),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "예약 상세 조회", description = "예약 ID로 본인 소유의 예약 상세를 조회합니다. (인증 필요)")
    @GetMapping("/reservations/{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.of(
                reservationService.getReservation(reservationId, user.getUser()),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }
}
