package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Movie;
import com.ceos22.cgv_clone.web.dto.MovieResponseDto;
import com.ceos22.cgv_clone.web.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<MovieResponseDto.MovieDetailDto> getMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieResponseDto.MovieDetailDto> dtoList = movieList.stream()
                .map(MovieResponseDto.MovieDetailDto::of)
                .collect(Collectors.toList());

        return dtoList;
    }

    public MovieResponseDto.MovieDetailDto getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .map(MovieResponseDto.MovieDetailDto::of)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found"));
    }

}
