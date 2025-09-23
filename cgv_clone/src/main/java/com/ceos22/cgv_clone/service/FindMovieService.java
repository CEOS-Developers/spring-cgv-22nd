package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.MovieDto;
import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import com.ceos22.cgv_clone.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindMovieService {
    private final MovieRepository movieRepository;

    /** 단건 조회 */
    public MovieDto getById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다. id=" + movieId));
        return MovieDto.from(movie);
    }

    /** 전체 페이징 조회 */
    public Page<MovieDto> getPage(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(MovieDto::from);
    }

    /** 제목 검색 (페이징) */
    public Page<MovieDto> searchByTitle(String keyword, Pageable pageable) {
        return movieRepository.findByMovieTitleContainingIgnoreCase(keyword, pageable)
                .map(MovieDto::from);
    }
}
