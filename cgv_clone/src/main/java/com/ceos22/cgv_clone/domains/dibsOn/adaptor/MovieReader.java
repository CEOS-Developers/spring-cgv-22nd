package com.ceos22.cgv_clone.domains.dibsOn.adaptor;

import com.ceos22.cgv_clone.domains.dibsOn.dto.Movie;
import com.ceos22.cgv_clone.domains.reservationMovie.domain.MovieEntity;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieReader {

    private final MovieRepository movieRepository;

    /** 영화 단건 조회 */
    public Movie findById(Long movieId) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow();
        return Movie.from(movie);
    }
}
