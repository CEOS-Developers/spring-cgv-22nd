package com.ceos22.cgv_clone.service;


import com.ceos22.cgv_clone.domain.movie.*;
import com.ceos22.cgv_clone.dto.response.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    //전체 영화 조회
    public List<MovieResponseDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }

    // 영화 상세 조회
    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없음"));

        return MovieResponseDto.fromEntity(movie);
    }


    // 특정 영화 + 특정 극장의 상영 시간표 조회
    public List<ShowtimeResponseDto> getShowtimesByMovieAndTheater(Long movieId, Long theaterId) {
        return showtimeRepository.findByMovie_IdAndScreen_Theater_Id(movieId, theaterId)
                .stream()
                .map(ShowtimeResponseDto::fromEntity)
                .toList();
    }
}
