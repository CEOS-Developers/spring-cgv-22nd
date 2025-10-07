package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.User;
import com.ceos22.cgv_clone.domain.movie.Movie;
import com.ceos22.cgv_clone.domain.movie.MovieFavorite;
import com.ceos22.cgv_clone.domain.theater.Theater;
import com.ceos22.cgv_clone.domain.theater.TheaterFavorite;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final MovieFavoriteRepository movieFavoriteRepository;
    private final TheaterRepository theaterRepository;
    private final TheaterFavoriteRepository theaterFavoriteRepository;

    // 영화 찜 추가
    public void addMovieFavorite(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화 없음"));

        // 중복 체크
        movieFavoriteRepository.findByUserAndMovie(user, movie)
                .ifPresent(f -> { throw new IllegalStateException("이미 찜한 영화입니다."); });

        MovieFavorite favorite = MovieFavorite.builder()
                .user(user)
                .movie(movie)
                .build();

        movieFavoriteRepository.save(favorite);
    }

    // 영화 찜 삭제
    public void removeMovieFavorite(String username, Long movieId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화 없음"));

        MovieFavorite favorite = movieFavoriteRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new IllegalArgumentException("찜하지 않은 영화입니다."));

        movieFavoriteRepository.delete(favorite);
    }

    // 영화관 찜 추가
    public void addTheaterFavorite(String username, Long theaterId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화관입니다."));

        theaterFavoriteRepository.findByUserAndTheater(user, theater)
                .ifPresent(f -> { throw new IllegalStateException("이미 찜한 영화관입니다."); });

        theaterFavoriteRepository.save(TheaterFavorite.builder()
                .user(user)
                .theater(theater)
                .build());
    }

    // 영화관 찜 삭제
    public void removeTheaterFavorite(String username, Long theaterId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화관입니다."));

        TheaterFavorite fav = theaterFavoriteRepository.findByUserAndTheater(user, theater)
                .orElseThrow(() -> new IllegalArgumentException("찜하지 않은 영화관입니다."));

        theaterFavoriteRepository.delete(fav);
    }
}
