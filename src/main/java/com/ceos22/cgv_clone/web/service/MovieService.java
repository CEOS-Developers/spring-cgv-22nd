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

    public ResponseEntity<List<MovieResDto.MovieDetailDto>> getMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieResDto.MovieDetailDto> dtoList = movieList.stream()
                .map(MovieResDto.MovieDetailDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<MovieResDto.MovieDetailDto> getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found with id: " + movieId));

        MovieResDto.MovieDetailDto movieDetailDto = MovieResDto.MovieDetailDto.of(movie);

        return ResponseEntity.ok(movieDetailDto);
    }

}
