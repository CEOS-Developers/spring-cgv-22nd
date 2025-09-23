package com.ceos22.cgv_clone.domain.reservation.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.request.ReservationCreateRequest;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ReservationDetailResponse;
import com.ceos22.cgv_clone.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<List<ReservationDetailResponse>>> reserveSeat(
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        List<ReservationDetailResponse> reservations = reservationService.createReservation(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, reservations);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ReservationDetailResponse>>> getMyReservations(
            @RequestParam Long memberId // 로그인 구현 후 변경
    ) {
        List<ReservationDetailResponse> reservations = reservationService.findMyReservations(memberId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, reservations);
    }
}
