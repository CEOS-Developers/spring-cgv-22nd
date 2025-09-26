package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.domain.Movie;
import com.ceos22.cgv_clone.web.domain.MoviePrefer;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.dto.MovieRequestDto;
import com.ceos22.cgv_clone.web.dto.MovieResponseDto;
import com.ceos22.cgv_clone.web.repository.MoviePreferRepository;
import com.ceos22.cgv_clone.web.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final MoviePreferRepository moviePreferRepository;

    public List<MovieResponseDto.MovieDetailDto> getMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<MovieResponseDto.MovieDetailDto> dtoList = movieList.stream()
                .map(MovieResponseDto.MovieDetailDto::of)
                .collect(Collectors.toList());

        return dtoList;
    }

    public MovieResponseDto.MovieDetailDto getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .map(MovieResponseDto.MovieDetailDto::of)
                .orElseThrow(()-> new IllegalArgumentException("Movie not found"));
    }

    public void preferMovie(Long movieId, User user) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.MOVIE_NOT_FOUND));


        if (moviePreferRepository.existsByMovieAndUser(movie,user)){
            throw new GeneralException(ErrorStatus.ALREADY_PREFERED_MOVIE);
        }

        MoviePrefer moviePrefer = MoviePrefer.builder()
                .movie(movie)
                .user(user)
                .build();
        moviePreferRepository.save(moviePrefer);

    }

    @Transactional
    public MovieResponseDto.MovieDetailDto createMovie(MovieRequestDto movieRequestDto) {

        boolean exists = movieRepository.existsByTitleAndReleaseDate(
                movieRequestDto.getTitle(), movieRequestDto.getReleaseDate());
        if (exists) {
            throw new GeneralException(ErrorStatus.ALREADY_EXISTS_MOVIE);
        }

        Movie movie = Movie.builder()
                .title(movieRequestDto.getTitle())
                .releaseDate(movieRequestDto.getReleaseDate())
                .runningTime(movieRequestDto.getRunningTime())
                .poster(movieRequestDto.getPoster())
                .genre(movieRequestDto.getGenre())
                .prolog(movieRequestDto.getProlog())
                .ageRating(movieRequestDto.getAgeRating())
                .build();

        movieRepository.save(movie);

        return MovieResponseDto.MovieDetailDto.of(movie);
    }
}
