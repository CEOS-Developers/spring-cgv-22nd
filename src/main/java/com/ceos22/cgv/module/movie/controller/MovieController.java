package com.ceos22.cgv.module.movie.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.*;
import com.ceos22.cgv.module.movie.service.MovieService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("api/movies")
    public ResponseEntity<ApiResponse<MovieListResponse>> getMovieTitles(
            @RequestParam(required = false) String query) {

        ApiResponse<MovieListResponse> response = ApiResponse.<MovieListResponse>builder()
                .response(movieService.findMovieTitleList(query))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("api/movies/{movieId}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(
            @PathVariable Long movieId) {

        ApiResponse<MovieResponse> response = ApiResponse.<MovieResponse>builder()
                .response(movieService.findMovieById(movieId))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("api/movies/schedule")
    public ResponseEntity<ApiResponse<MovieScheduleListResponse>> getSchedule(
            @Valid @ModelAttribute ScheduleRequest request) {

        ApiResponse<MovieScheduleListResponse> response = ApiResponse.<MovieScheduleListResponse>builder()
                .response(movieService.findMovieSchedule(request))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/movies/{movieId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MovieLikeResponse>> postLike(
            @PathVariable Long movieId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<MovieLikeResponse> response = ApiResponse.<MovieLikeResponse>builder()
                .response(movieService.like(movieId, user.getUserId()))
                .statusCode(SuccessCode.INSERT_SUCCESS.getStatusCode())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/movies/{movieId}/unlike")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MovieLikeResponse>> unLike(
            @PathVariable Long movieId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<MovieLikeResponse> response = ApiResponse.<MovieLikeResponse>builder()
                .response(movieService.unlike(movieId, user.getUserId()))
                .statusCode(SuccessCode.DELETE_SUCCESS.getStatusCode())
                .message(SuccessCode.DELETE_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
}
