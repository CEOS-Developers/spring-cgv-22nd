package com.ceos22.cgv.module.cinema.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaLikeResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;

import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "영화관 목록 조회", description = "지역과 상영관 종류로 영화관 목록을 조회합니다.")
    @GetMapping("/cinemas")
    public ResponseEntity<ApiResponse<CinemaListResponse>> getCinemas(
            @Valid @ModelAttribute CinemaRequest request) {

        ApiResponse<CinemaListResponse> response = ApiResponse.of(
                cinemaService.findCinemas(request),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "영화관 조회", description = "영화관 id로 특정 영화관을 조회합니다.")
    @GetMapping("/cinemas/{cinemaId}")
    public ResponseEntity<ApiResponse<CinemaResponse>> getCinema(
            @PathVariable Long cinemaId) {

        ApiResponse<CinemaResponse> response = ApiResponse.of(
                cinemaService.findCinemaById(cinemaId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "영화관 찜 추가", description = "특정 영화관에 찜을 추가합니다. (인증 필요)")
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

    @Operation(summary = "영화관 찜 제거", description = "특정 영화관에서 찜을 제거합니다. (인증 필요)")
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
