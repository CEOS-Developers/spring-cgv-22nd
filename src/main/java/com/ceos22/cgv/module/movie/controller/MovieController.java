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
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ReservationService reservationService;

    @Operation(summary = "영화 목록 조회", description = "영화 제목을 검색어로 필터링하여 목록을 조회합니다. 검색어가 없으면 전체 목록을 반환합니다.")
    @GetMapping("/movies")
    public ResponseEntity<ApiResponse<MovieListResponse>> getMovieTitles(
            @RequestParam(required = false) String query) {

        ApiResponse<MovieListResponse> response = ApiResponse.of(
                movieService.findMovieTitleList(query),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "영화 상세 조회", description = "영화 ID로 특정 영화의 상세 정보를 조회합니다.")
    @GetMapping("/movies/{movieId}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(
            @PathVariable Long movieId) {

        ApiResponse<MovieResponse> response = ApiResponse.of(
                movieService.findMovieById(movieId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상영 일정 조회", description = "영화/지역/극장/날짜 등 조건으로 상영 일정을 조회합니다.")
    @GetMapping("/movies/schedule")
    public ResponseEntity<ApiResponse<MovieScheduleListResponse>> getSchedule(
            @Valid @ModelAttribute ScheduleRequest request) {

        ApiResponse<MovieScheduleListResponse> response = ApiResponse.of(
                movieService.findMovieSchedule(request),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "좌석 예약 현황 조회", description = "스케줄 ID로 상영관 좌석의 예약(점유) 상태를 조회합니다.")
    @GetMapping("/movies/schedule/{scheduleId}/availability")
    public ResponseEntity<ApiResponse<SeatAvailabilityResponse>> availability(
            @PathVariable Long scheduleId) {

        ApiResponse<SeatAvailabilityResponse> response = ApiResponse.of(
                reservationService.availability(scheduleId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "영화 찜 추가", description = "특정 영화에 대해 현재 로그인한 사용자의 찜을 추가합니다. (인증 필요)")
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

    @Operation(summary = "영화 찜 제거", description = "특정 영화에 대해 현재 로그인한 사용자의 찜을 제거합니다. (인증 필요)")
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
