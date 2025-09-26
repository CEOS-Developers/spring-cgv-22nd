package com.ceos22.cgv.module.reservation.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.MovieLikeResponse;
import com.ceos22.cgv.module.reservation.dto.ReservationRequest;
import com.ceos22.cgv.module.reservation.dto.ReservationResponse;
import com.ceos22.cgv.module.reservation.service.ReservationService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.response.ApiResponse;
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

    @PostMapping("api/reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @RequestBody ReservationRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.<ReservationResponse>builder()
                .response(reservationService.reserve(request, user.getUserId()))
                .statusCode(SuccessCode.INSERT_SUCCESS.getStatusCode())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/reservations/{reservationId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> cancel(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.<ReservationResponse>builder()
                .response(reservationService.cancel(reservationId, user.getUserId()))
                .statusCode(SuccessCode.INSERT_SUCCESS.getStatusCode())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("api/reservations")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> myReservations(
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<List<ReservationResponse>> response = ApiResponse.<List<ReservationResponse>>builder()
                .response(reservationService.getMyReservations(user.getUserId()))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("api/reservations/{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<ReservationResponse> response = ApiResponse.<ReservationResponse>builder()
                .response(reservationService.getReservation(reservationId, user.getUserId()))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
}
