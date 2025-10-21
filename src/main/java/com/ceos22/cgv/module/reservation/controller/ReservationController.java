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

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

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
