package com.ceos22.cgv.module.movie.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.MovieListResponse;
import com.ceos22.cgv.module.movie.dto.MovieRequest;
import com.ceos22.cgv.module.movie.dto.MovieResponse;
import com.ceos22.cgv.module.movie.service.MovieService;
import com.ceos22.cgv.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("api/movies")
    public ResponseEntity<ApiResponse<MovieListResponse>> getMovies(
            @RequestParam(required = false) MovieRequest movieRequest) {

        ApiResponse<MovieListResponse> response = ApiResponse.<MovieListResponse>builder()
                .response(movieService.findMovies(movieRequest))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("api/movies/{movie_id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovie(
            @PathVariable Long movieId) {

        ApiResponse<MovieResponse> response = ApiResponse.<MovieResponse>builder()
                .response(movieService.findMovieById(movieId))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
}
