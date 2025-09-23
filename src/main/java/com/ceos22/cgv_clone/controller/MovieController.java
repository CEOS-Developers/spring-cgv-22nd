package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.domain.Theater;
import com.ceos22.cgv_clone.dto.movie.MovieHomeDto;
import com.ceos22.cgv_clone.dto.movie.MovieDetailDto;
import com.ceos22.cgv_clone.dto.movie.ShowtimeResponseDto;
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
    public List<MovieHomeDto> getHomeMovies() {
        return movieService.getHomeMovies();
    }

    @GetMapping("/{id}")
    public MovieDetailDto getMovieDetail(@PathVariable Long id) {
        return movieService.getMovieDetail(id);
    }

    // 영화별 극장 목록 조회
    @GetMapping("/{movieId}/theaters")
    public List<Theater> getTheatersByMovie(@PathVariable Long movieId) {
        return movieService.getTheatersByMovie(movieId);
    }

    // 영화 + 극장 상영 시간표 조회
    @GetMapping("/{movieId}/theaters/{theaterId}/showtimes")
    public List<ShowtimeResponseDto> getShowtimes(
            @PathVariable Long movieId,
            @PathVariable Long theaterId) {
        return movieService.getShowtimes(movieId, theaterId);
    }
}
