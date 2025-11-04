package com.ceos22.cgv_clone.domain.favorite.service;

import com.ceos22.cgv_clone.domain.favorite.dto.FavoriteTargetType;
import com.ceos22.cgv_clone.domain.favorite.dto.response.FavoriteResponse;
import com.ceos22.cgv_clone.domain.favorite.repository.MovieFavoriteRepository;
import com.ceos22.cgv_clone.domain.favorite.repository.TheaterFavoriteRepository;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.favorite.entity.MovieFavorite;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.favorite.entity.TheaterFavorite;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    @Transactional
    public FavoriteResponse addMovieFavorite(String username, Long movieId) {
        User user = getUser(username);
        Movie movie = getMovie(movieId);

        movieFavoriteRepository.findByUserAndMovie(user, movie)
                .ifPresent(f -> {
                    throw new BusinessException(
                            ErrorCode.FAVORITE_DUPLICATED,
                            "이미 찜한 영화입니다.(username=%s, movieId=%d)".formatted(username, movieId));});

        MovieFavorite saved = movieFavoriteRepository.save(MovieFavorite.create(user, movie));

        return FavoriteResponse.of(saved.getId(), FavoriteTargetType.MOVIE);
    }

   @Transactional
    public void removeMovieFavorite(String username, Long movieId) {
        User user = getUser(username);
        Movie movie = getMovie(movieId);

        MovieFavorite favorite = movieFavoriteRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FAVORITE_NOT_FOUND,
                        "찜하지 않은 영화입니다.(username=%s, movieId=%d)".formatted(username, movieId)));

        movieFavoriteRepository.delete(favorite);
    }

    @Transactional
    public FavoriteResponse addTheaterFavorite(String username, Long theaterId) {
        User user = getUser(username);
        Theater theater = getTheater(theaterId);

        theaterFavoriteRepository.findByUserAndTheater(user, theater)
                .ifPresent(f -> {
                    throw new BusinessException(ErrorCode.FAVORITE_DUPLICATED,
                        "이미 찜한 영화관입니다.(username=%s, theaterId=%d)".formatted(username, theaterId));});

        TheaterFavorite saved = theaterFavoriteRepository.save(TheaterFavorite.create(user, theater));

        return new FavoriteResponse(saved.getId(), FavoriteTargetType.THEATER);
    }

    @Transactional
    public void removeTheaterFavorite(String username, Long theaterId) {
        User user = getUser(username);
        Theater theater = getTheater(theaterId);

        TheaterFavorite fav = theaterFavoriteRepository.findByUserAndTheater(user, theater)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FAVORITE_NOT_FOUND,
                        "이미 찜한 영화관입니다.(username=%s, theaterId=%d)".formatted(username, theaterId)
                ));

        theaterFavoriteRepository.delete(fav);

    }

    /* error handler */
    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "username=%s".formatted(username)));
    }

    private Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MOVIE_NOT_FOUND, "movieId=%d".formatted(movieId)));
    }

    private Theater getTheater(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEATER_NOT_FOUND, "theaterId=%d".formatted(theaterId)));
    }

}
