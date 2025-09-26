package com.ceos22.cgv.module.cinema.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaLikeResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;

import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.module.movie.dto.MovieLikeResponse;
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

    @GetMapping("api/cinemas")
    public ResponseEntity<ApiResponse<CinemaListResponse>> getCinemas(
            @Valid @ModelAttribute CinemaRequest request) {

        ApiResponse<CinemaListResponse> response = ApiResponse.<CinemaListResponse>builder()
                .response(cinemaService.findCinemas(request))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("api/cinemas/{cinemaId}")
    public ResponseEntity<ApiResponse<CinemaResponse>> getCinema(
            @PathVariable Long cinemaId) {

        ApiResponse<CinemaResponse> response = ApiResponse.<CinemaResponse>builder()
                .response(cinemaService.findCinemaById(cinemaId))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/cinemas/{cinemaId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CinemaLikeResponse>> postLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<CinemaLikeResponse> response = ApiResponse.<CinemaLikeResponse>builder()
                .response(cinemaService.like(cinemaId, user.getUserId()))
                .statusCode(SuccessCode.INSERT_SUCCESS.getStatusCode())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/cinemas/{cinemaId}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CinemaLikeResponse>> unLike(
            @PathVariable Long cinemaId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<CinemaLikeResponse> response = ApiResponse.<CinemaLikeResponse>builder()
                .response(cinemaService.unlike(cinemaId, user.getUserId()))
                .statusCode(SuccessCode.DELETE_SUCCESS.getStatusCode())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

}
