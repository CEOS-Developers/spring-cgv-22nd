package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.movie.dto.response.MovieDetailResponse;
import com.ceos22.cgv_clone.domain.movie.dto.response.MovieSimpleResponse;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MovieSimpleResponse>>> getNowPlayingMovies() {
        List<MovieSimpleResponse> movies = movieService.findAllNowPlayingMovies();
        return ApiResponse.success(SuccessCode.GET_SUCCESS, movies);
    }

    @GetMapping("/{movieid}")
    public ResponseEntity<ApiResponse<MovieDetailResponse>> getMovieDetail(@PathVariable Long movieid) {
        MovieDetailResponse movieDetail = movieService.findMovieById(movieid);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, movieDetail);
    }
}
