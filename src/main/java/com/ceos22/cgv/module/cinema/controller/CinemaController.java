package com.ceos22.cgv.module.cinema.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaLikeResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;

import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping("/cinemas")
    public ResponseEntity<ApiResponse<CinemaListResponse>> getCinemas(
            @Valid @ModelAttribute CinemaRequest request) {

        ApiResponse<CinemaListResponse> response = ApiResponse.of(
                cinemaService.findCinemas(request),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/cinemas/{cinemaId}")
    public ResponseEntity<ApiResponse<CinemaResponse>> getCinema(
            @PathVariable Long cinemaId) {

        ApiResponse<CinemaResponse> response = ApiResponse.of(
                cinemaService.findCinemaById(cinemaId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/cinemas/{cinemaId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CinemaLikeResponse>> postLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<CinemaLikeResponse> response = ApiResponse.of(
                cinemaService.like(cinemaId, user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/cinemas/{cinemaId}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CinemaLikeResponse>> unLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<CinemaLikeResponse> response = ApiResponse.of(
                cinemaService.unlike(cinemaId, user.getUser()),
                SuccessCode.DELETE_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

}
