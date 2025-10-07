package com.ceos22.cgv_clone.domain.movie.service;

import com.ceos22.cgv_clone.domain.movie.dto.MovieDto;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieDto> getMovieList() {
        return movieRepository.findAll()
                .stream().map(MovieDto::of).toList();
    }

    public MovieDto getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return MovieDto.of(movie);
    }
}
