package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.Movie;
import com.ceos22.cgv_clone.web.dto.CinemaResDto;
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

    public ResponseEntity<List<MovieResDto.MovieDto>> getMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieResDto.MovieDto> dtoList = movieList.stream()
                .map(MovieResDto.MovieDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<MovieResDto.MovieDto> getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found with id: " + movieId));

        MovieResDto.MovieDto movieDto = MovieResDto.MovieDto.of(movie);

        return ResponseEntity.ok(movieDto);
    }
}
