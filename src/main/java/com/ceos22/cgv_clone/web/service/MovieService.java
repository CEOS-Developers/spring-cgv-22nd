package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Movie;
import com.ceos22.cgv_clone.web.dto.MovieResDto;
import com.ceos22.cgv_clone.web.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<MovieResDto.MovieDetailDto> getMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieResDto.MovieDetailDto> dtoList = movieList.stream()
                .map(MovieResDto.MovieDetailDto::of)
                .collect(Collectors.toList());

        return dtoList;
    }

    public MovieResDto.MovieDetailDto getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .map(MovieResDto.MovieDetailDto::of)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found"));
    }

}
