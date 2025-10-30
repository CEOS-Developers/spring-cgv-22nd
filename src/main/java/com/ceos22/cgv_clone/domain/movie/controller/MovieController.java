package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.domain.movie.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public List<MovieResponseDto> getAllMovies() {
        log.info("[GET] /movies 요청 수신");
        List<MovieResponseDto> movies = movieService.getAllMovies();
        return movies;
    }

    @GetMapping("/movies/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        log.info("[GET] /movies/{} 요청 수신", id);
        MovieResponseDto movie = movieService.getMovieById(id);
        return movie;
    }

    // 영화 + 극장 상영 시간표 조회
    @GetMapping("/movies/{movieId}/theaters/{theaterId}/showtimes")
    public List<ShowtimeResponseDto> getShowtimes(@PathVariable Long movieId, @PathVariable Long theaterId) {
        log.info("[GET] /movies/{}/theaters/{}/showtimes 요청 수신", movieId, theaterId);
        List<ShowtimeResponseDto> showtimes = movieService.getShowtimesByMovieAndTheater(movieId, theaterId);
        return showtimes;
    }
}
