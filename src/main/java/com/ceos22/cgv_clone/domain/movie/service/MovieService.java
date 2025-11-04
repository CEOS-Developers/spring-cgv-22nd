package com.ceos22.cgv_clone.domain.movie.service;


import com.ceos22.cgv_clone.domain.movie.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;

    //전체 영화 조회
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getAllMovies() {
        List<MovieResponseDto> movies = movieRepository.findAll().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();

        return movies;
    }

    // 영화 상세 조회
    @Transactional(readOnly = true)
    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> { return new BusinessException(ErrorCode.MOVIE_NOT_FOUND, "movieId=%d인 영화를 찾을 수 없습니다.".formatted(id));});
        return MovieResponseDto.fromEntity(movie);
    }

    // 특정 영화 + 특정 극장의 상영 시간표 조회
    public List<ShowtimeResponseDto> getShowtimesByMovieAndTheater(Long movieId, Long theaterId) {
        if(!movieRepository.existsById(movieId)) {
            throw new BusinessException(ErrorCode.MOVIE_NOT_FOUND, "movieId=%d인 영화를 찾을 수 없습니다.".formatted(movieId));
        }

        if(!theaterRepository.existsById(theaterId)) {
            throw new BusinessException(ErrorCode.THEATER_NOT_FOUND, "theaterId=%d인 영화를 찾을 수 없습니다.".formatted(theaterId));
        }

        List<ShowtimeResponseDto> showtimes = showtimeRepository
                .findByMovie_IdAndScreen_Theater_Id(movieId, theaterId)
                .stream()
                .map(ShowtimeResponseDto::fromEntity)
                .toList();
        return showtimes;

    }
}
