package com.ceos22.cgv_clone.service;


import com.ceos22.cgv_clone.domain.movie.dto.MovieDto;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    @DisplayName("영화 목록 전체 조회 - 성공")
    void getMovieListSuccess() {
        // given
        Movie movie1 = new Movie();
        Movie movie2 = new Movie();
        List<Movie> movies = List.of(movie1, movie2);

        given(movieRepository.findAll()).willReturn(movies);

        // when
        List<MovieDto> result = movieService.getMovieList();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getMovieId()).isEqualTo(movie1.getId());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("영화 상세 조회 - 성공")
    void getMovieSuccess() {
        // given
        long movieId = 1L;
        Movie movie = new Movie();
        given(movieRepository.findById(movieId)).willReturn(Optional.of(movie));

        // when
        MovieDto result = movieService.getMovie(movieId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMovieId()).isEqualTo(movie.getId());
    }
}