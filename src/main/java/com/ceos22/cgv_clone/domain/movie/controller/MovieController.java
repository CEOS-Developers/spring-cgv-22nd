package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.domain.movie.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    // 전체 영화 조회
    @GetMapping("/movies")
    public ApiResponse<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieService.getAllMovies();
        return ApiResponse.of(movies, SuccessCode.GET_SUCCESS);
    }

    // 영화 상세 조회
    @GetMapping("/movies/{id}")
    public ApiResponse<MovieResponseDto> getMovieById(@PathVariable Long id) {
        MovieResponseDto movie = movieService.getMovieById(id);
        return ApiResponse.of(movie, SuccessCode.GET_SUCCESS);
    }

    // 영화 + 극장 상영 시간표 조회
    @GetMapping("/movies/{movieId}/theaters/{theaterId}/showtimes")
    public ApiResponse<List<ShowtimeResponseDto>> getShowtimes(@PathVariable Long movieId, @PathVariable Long theaterId) {
        List<ShowtimeResponseDto> showtimes = movieService.getShowtimesByMovieAndTheater(movieId, theaterId);
        return ApiResponse.of(showtimes, SuccessCode.GET_SUCCESS);
    }
}
