package com.ceos22.spring_boot.domain.movie.service;

import com.ceos22.spring_boot.domain.movie.FavoriteMovieRepository;
import com.ceos22.spring_boot.domain.movie.MovieRepository;
import com.ceos22.spring_boot.domain.movie.entity.FavoriteMovie;
import com.ceos22.spring_boot.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteMovieService {
    private final FavoriteMovieRepository favorites;
    private final MovieRepository movies;
    private final UserRepository users;

    public void add(Long userId, Long movieId) {
        if (favorites.existsByUser_UserIdAndMovie_MovieId(userId, movieId)) return;
        var user = users.findById(userId).orElseThrow();
        var movie = movies.findById(movieId).orElseThrow();
        favorites.save(FavoriteMovie.builder().user(user).movie(movie).build());
    }

    public void remove(Long userId, Long movieId) {
        favorites.findByUser_UserIdAndMovie_MovieId(userId, movieId)
                .ifPresent(favorites::delete);
    }

    public long count(Long movieId) {
        return favorites.countByMovie_MovieId(movieId);
    }
}
