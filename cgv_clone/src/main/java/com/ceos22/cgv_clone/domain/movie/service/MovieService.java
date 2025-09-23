package com.ceos22.cgv_clone.domain.movie.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.movie.dto.request.MovieCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.MovieDetailResponse;
import com.ceos22.cgv_clone.domain.movie.dto.response.MovieSimpleResponse;
import com.ceos22.cgv_clone.domain.movie.entity.Actor;
import com.ceos22.cgv_clone.domain.movie.entity.Director;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.entity.MovieStatus;
import com.ceos22.cgv_clone.domain.movie.repository.ActorRepository;
import com.ceos22.cgv_clone.domain.movie.repository.DirectorRepository;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    public List<MovieSimpleResponse> findAllNowPlayingMovies() {
        List<Movie> movies = movieRepository.findByStatus(MovieStatus.NOW_PLAYING);

        return movies.stream()
                .map(MovieSimpleResponse::from)
                .collect(Collectors.toList());
    }

    public MovieDetailResponse findMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId).
                orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        return MovieDetailResponse.from(movie);
    }

    @Transactional
    public MovieDetailResponse createMovie(MovieCreateRequest request) {

        Director director = directorRepository.findById(request.directorId())
                .orElseThrow(() -> new CustomException(ErrorCode.DIRECTOR_NOT_FOUND));

        List<Actor> actors = actorRepository.findAllById(request.actorIds());
        if(actors.size() != request.actorIds().size()) {
            throw new CustomException(ErrorCode.ACTOR_NOT_FOUND);
        }

        Movie movie = Movie.builder()
                .title(request.title())
                .genre(request.genre())
                .status(request.status())
                .runningTime(request.runningTime())
                .posterUrl(request.posterUrl())
                .director(director)
                .actors(actors)
                .build();

        Movie savedMovie = movieRepository.save(movie);

        return MovieDetailResponse.from(savedMovie);
    }
}
