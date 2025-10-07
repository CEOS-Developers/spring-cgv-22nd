package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.domain.theater.Theater;
import com.ceos22.cgv_clone.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // 영화 + 극장 상영 시간표 조회
    @GetMapping("/{movieId}/theaters/{theaterId}/showtimes")
    public List<ShowtimeResponseDto> getShowtimes(@PathVariable Long movieId, @PathVariable Long theaterId) {
        return movieService.getShowtimesByMovieAndTheater(movieId, theaterId);
    }
}
