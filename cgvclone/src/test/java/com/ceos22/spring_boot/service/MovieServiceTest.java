package com.ceos22.spring_boot.service;

import com.ceos22.spring_boot.domain.movie.dto.MovieDto;
import com.ceos22.spring_boot.domain.movie.service.MovieService;
import com.ceos22.spring_boot.domain.movie.entity.Movie;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie1 = new Movie();
        setMovie(movie1, 1L, "Inception");

        movie2 = new Movie();
        setMovie(movie2, 2L, "Interstellar");
    }

    @Test
    void getAllMovies_returnsPageOfMovieDtos() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("movieId").ascending());
        Page<Movie> page = new PageImpl<>(List.of(movie1, movie2), pageable, 2);
        when(movieRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<MovieDto> result = movieService.getAllMovies(pageable);
        log.info(">>> getAllMovies result = {}", result.getContent());

        // then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Inception");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("Interstellar");

        verify(movieRepository, times(1)).findAll(pageable);
    }

    @Test
    void getMovieById_found_returnsDto() {
        // given
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie1));

        // when
        MovieDto dto = movieService.getMovieById(1L);
        log.info(">>> getMovieById(1) result = {}", dto);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getMovieId()).isEqualTo(1L);
        assertThat(dto.getTitle()).isEqualTo("Inception");

        verify(movieRepository).findById(1L);
    }

    @Test
    void getMovieById_notFound_returnsNull() {
        // given
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        // when
        MovieDto dto = movieService.getMovieById(99L);
        log.info(">>> getMovieById(99) result = {}", dto);

        // then
        assertThat(dto).isNull();
        verify(movieRepository).findById(99L);
    }

    // ====== helper ======
    private void setMovie(Movie movie, Long id, String title) {
        // Movie 엔티티는 @Setter 없으므로 리플렉션 또는 생성자/builder를 이용해야 함.

        try {
            var idField = Movie.class.getDeclaredField("movieId");
            idField.setAccessible(true);
            idField.set(movie, id);

            var titleField = Movie.class.getDeclaredField("title");
            titleField.setAccessible(true);
            titleField.set(movie, title);

            var releaseDateField = Movie.class.getDeclaredField("releaseDate");
            releaseDateField.setAccessible(true);
            releaseDateField.set(movie, new Date());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
