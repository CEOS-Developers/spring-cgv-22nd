package com.ceos22.spring_boot.domain.movie.service;

import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.movie.repository.FavoriteMovieRepository;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import com.ceos22.spring_boot.domain.movie.entity.FavoriteMovie;
import com.ceos22.spring_boot.domain.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class FavoriteMovieService {
    private final FavoriteMovieRepository favorites;
    private final MovieRepository movies;
    private final UserRepository users;

    @Transactional
    public void add(Long userId, Long movieId) {

        var user = users.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        var movie = movies.findById(movieId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MOVIE_NOT_FOUND));

        if (favorites.existsByUser_UserIdAndMovie_MovieId(userId, movieId)) {
            throw new GeneralException(ErrorStatus.ALREADY_FAVORITED, "이미 즐겨찾기된 영화입니다.");
        }

        favorites.save(FavoriteMovie.builder()
                .user(user)
                .movie(movie)
                .build());
    }
    @Transactional
    public void remove(Long userId, Long movieId) {
        var fav = favorites.findByUser_UserIdAndMovie_MovieId(userId, movieId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FAVORITE_NOT_FOUND, "즐겨찾기 내역이 없습니다."));
        favorites.delete(fav);
    }

    public long count(Long movieId) {
        return favorites.countByMovie_MovieId(movieId);
    }
}
