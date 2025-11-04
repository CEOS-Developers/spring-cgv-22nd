package com.ceos22.cgv_clone.domains.dibsOn.service;

import com.ceos22.cgv_clone.domains.dibsOn.dto.Movie;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.MovieRepository;
import com.ceos22.cgv_clone.domains.dibsOn.adaptor.MovieReader;
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
    private final MovieReader movieReader;

    /** 단건 조회 */
    public Movie getById(Long movieId) {
        return movieReader.findById(movieId);
    }

    /** 전체 페이징 조회 */
    public Page<Movie> getPage(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(Movie::from);
    }

    /** 제목 검색 (페이징) */
    public Page<Movie> searchByTitle(String keyword, Pageable pageable) {
        return movieRepository.findByMovieTitleContainingIgnoreCase(keyword, pageable)
                .map(Movie::from);
    }
}
