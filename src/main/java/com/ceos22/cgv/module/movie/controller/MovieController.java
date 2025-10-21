package com.ceos22.cgv.module.movie.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.*;
import com.ceos22.cgv.module.movie.service.MovieService;
import com.ceos22.cgv.module.reservation.dto.SeatAvailabilityResponse;
import com.ceos22.cgv.module.reservation.service.ReservationService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ReservationService reservationService;

    @GetMapping("/movies")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MovieListResponse>> getMovieTitles(
            @RequestParam(required = false) String query) {

        ApiResponse<MovieListResponse> response = ApiResponse.of(
                movieService.findMovieTitleList(query),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/movies/{movieId}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(
            @PathVariable Long movieId) {

        ApiResponse<MovieResponse> response = ApiResponse.of(
                movieService.findMovieById(movieId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/movies/schedule")
    public ResponseEntity<ApiResponse<MovieScheduleListResponse>> getSchedule(
            @Valid @ModelAttribute ScheduleRequest request) {

        ApiResponse<MovieScheduleListResponse> response = ApiResponse.of(
                movieService.findMovieSchedule(request),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    // 좌석 예약 상태 반환
    @GetMapping("/movies/schedule/{scheduleId}/availability")
    public ResponseEntity<ApiResponse<SeatAvailabilityResponse>> availability(
            @PathVariable Long scheduleId) {

        ApiResponse<SeatAvailabilityResponse> response = ApiResponse.of(
                reservationService.availability(scheduleId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/movies/{movieId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MovieLikeResponse>> postLike(
            @PathVariable Long movieId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<MovieLikeResponse> response = ApiResponse.of(
                movieService.like(movieId, user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/movies/{movieId}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MovieLikeResponse>> unLike(
            @PathVariable Long movieId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<MovieLikeResponse> response = ApiResponse.of(
                movieService.unlike(movieId, user.getUser()),
                SuccessCode.DELETE_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }
}
