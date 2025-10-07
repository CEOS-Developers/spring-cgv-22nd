package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.movie.dto.request.MovieCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.MovieDetailResponse;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<ApiResponse<MovieDetailResponse>> createMovie(@Valid @RequestBody MovieCreateRequest request) {
        MovieDetailResponse createdMovie = movieService.createMovie(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, createdMovie);
    }
}
