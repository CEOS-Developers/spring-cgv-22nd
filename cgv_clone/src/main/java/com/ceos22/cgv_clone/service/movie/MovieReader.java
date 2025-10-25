package com.ceos22.cgv_clone.service.movie;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;
import com.ceos22.cgv_clone.domain.dto.Movie;
import com.ceos22.cgv_clone.domain.reservationMovie.MovieEntity;
import com.ceos22.cgv_clone.repository.FavoriteMovieRepository;
import com.ceos22.cgv_clone.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieReader {

    private final MovieRepository movieRepository;

    /** 영화 단건 조회 */
    public Movie findById(Long movieId) {
        MovieEntity movie = movieRepository.findById(movieId)
                .orElseThrow(null);
        return Movie.from(movie);
    }
}
