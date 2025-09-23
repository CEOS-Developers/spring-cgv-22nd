package com.ceos22.cgv.module.movie.service;

import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;
import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.movie.dto.MovieListResponse;
import com.ceos22.cgv.module.movie.dto.MovieRequest;
import com.ceos22.cgv.module.movie.dto.MovieResponse;
import com.ceos22.cgv.module.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieListResponse findMovies(MovieRequest request) {
        return MovieListResponse.fromEntities(movieRepository.findAll());
    }

    public MovieResponse findMovieById(Long movieId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie not found: " + movieId));
        return MovieResponse.from(movie);
    }

}
