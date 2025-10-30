package com.ceos22.cgv_clone.domain.movie.service;


import com.ceos22.cgv_clone.domain.movie.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
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

    //전체 영화 조회
    public List<MovieResponseDto> getAllMovies() {
        log.debug("[SVC] getAllMovies() start");
        List<MovieResponseDto> movies = movieRepository.findAll().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();

        if (movies.isEmpty()) {
            // 요청한 데이터가 없지만 정상 흐름 → WARN
            log.warn("[SVC] 조회된 영화 데이터가 없음");
        } else {
            // 정상 흐름 → INFO
            log.info("[SVC] 영화 조회 성공");
        }
        return movies;
    }

    // 영화 상세 조회
    @Transactional
    public MovieResponseDto getMovieById(Long id) {
        log.debug("[SVC] getMovieById() start - id={}", id);

        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> {
                        return new IllegalArgumentException("영화를 찾을 수 없음");
                    });

            log.info("[SVC] 영화 상세 조회 완료 - id={}, title={}", movie.getId(), movie.getTitle());
            return MovieResponseDto.fromEntity(movie);

        }
        catch (IllegalArgumentException e) {
            // 사용자가 잘못된 입력을 전달했거나 데이터가 없음 -> WARN
            log.warn("[SVC] 잘못된 요청 또는 존재하지 않는 영화 ID={} - {}", id, e.getMessage());
            throw e;

        }
        catch (Exception e) {
            // 예기치 못한 DB 오류 등 -> ERROR
            log.error("[SVC] 영화 조회 중 알 수 없는 오류 발생 - id={}", id, e);
            throw e;
        }
    }


    // 특정 영화 + 특정 극장의 상영 시간표 조회
    public List<ShowtimeResponseDto> getShowtimesByMovieAndTheater(Long movieId, Long theaterId) {
        log.debug("[SVC] getShowtimesByMovieAndTheater() 호출됨 - movieId={}, theaterId={}", movieId, theaterId);

        try {
            List<ShowtimeResponseDto> showtimes = showtimeRepository
                    .findByMovie_IdAndScreen_Theater_Id(movieId, theaterId)
                    .stream()
                    .map(ShowtimeResponseDto::fromEntity)
                    .toList();

            if (showtimes.isEmpty()) {
                // 정상적으로 처리되지만 데이터 없음 -> WARN
                log.warn("[SVC] 상영 시간표 없음 - movieId={}, theaterId={}", movieId, theaterId);
            }
            else {
                log.info("[SVC] 상영 시간표 조회 완료 - movieId={}, theaterId={}, 총 {}건",
                        movieId, theaterId, showtimes.size());
            }

            return showtimes;

        }
        catch (Exception e) {
            // DB 접근 실패나 예외 -> ERROR
            log.error("[SVC] 상영 시간표 조회 실패 - movieId={}, theaterId={}", movieId, theaterId, e);
            throw e;
        }
    }
}
