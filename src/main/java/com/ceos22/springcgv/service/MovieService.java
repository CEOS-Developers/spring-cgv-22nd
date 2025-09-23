package com.ceos22.springcgv.service;

import com.ceos22.springcgv.domain.Movie;
import com.ceos22.springcgv.dto.MovieDto;
import com.ceos22.springcgv.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * 영화 차트 목록을 예매율 순으로 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<MovieDto> getMovieChartList() {
        // 예매율 순
        List<Movie> movies = movieRepository.findAllByOrderByBookingRateDesc();

        return movies.stream()
                .map(MovieDto::from)
                .collect(Collectors.toList());
    }
}