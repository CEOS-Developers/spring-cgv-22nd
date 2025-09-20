package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.web.dto.CinemaResDto;
import com.ceos22.cgv_clone.web.dto.MovieResDto;
import com.ceos22.cgv_clone.web.repository.MovieRepository;
import com.ceos22.cgv_clone.web.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping()
    public ResponseEntity<List<MovieResDto.MovieDto>> getMovies(){
        return movieService.getMovies();
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResDto.MovieDto> getMovie(@PathVariable(name = "movieId")Long movieId){
        return movieService.getMovie(movieId);
    }
}
