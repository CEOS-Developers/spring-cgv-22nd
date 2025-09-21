package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.dto.MovieResDto;
import com.ceos22.cgv_clone.web.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping()
    public ResponseEntity<List<MovieResDto.MovieDetailDto>> getMovies(){
        return movieService.getMovies();
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResDto.MovieDetailDto> getMovie(@PathVariable(name = "movieId")Long movieId){
        return movieService.getMovie(movieId);
    }



}
