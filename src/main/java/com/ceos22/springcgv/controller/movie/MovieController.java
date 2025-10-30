package com.ceos22.springcgv.controller.movie;

import com.ceos22.springcgv.dto.movie.MovieDto;
import com.ceos22.springcgv.service.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;


    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getMovies() {
        List<MovieDto> movieChart = movieService.getMovieChartList();
        return ResponseEntity.ok(movieChart);
    }
}