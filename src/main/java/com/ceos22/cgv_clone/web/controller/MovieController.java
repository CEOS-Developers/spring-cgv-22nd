package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.dto.MovieResponseDto;
import com.ceos22.cgv_clone.web.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieResponseDto.MovieDetailDto>> getMovies(){
        return ResponseEntity.ok(movieService.getMovies());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<MovieResponseDto.MovieDetailDto> getMovie(@PathVariable(name = "movieId")Long movieId){
        return ResponseEntity.ok(movieService.getMovie(movieId));
    }



}
