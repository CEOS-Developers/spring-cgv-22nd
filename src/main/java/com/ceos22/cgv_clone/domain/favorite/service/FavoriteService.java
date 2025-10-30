package com.ceos22.cgv_clone.domain.favorite.service;

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
    public void addMovieFavorite(String username, Long movieId) {
        log.debug("[SVC] addMovieFavorite() start - user={}, movieId={}", username, movieId);
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("영화 없음"));

            movieFavoriteRepository.findByUserAndMovie(user, movie)
                    .ifPresent(f -> { throw new IllegalStateException("이미 찜한 영화입니다."); });

            MovieFavorite favorite = MovieFavorite.builder().user(user).movie(movie).build();
            movieFavoriteRepository.save(favorite);

            log.info("[SVC] 영화 찜 등록 완료 - user={}, movieId={}", username, movieId);
        }
        catch (IllegalStateException e) {
            // 비정상 요청(중복) -> WARN
            log.warn("[SVC] 중복 찜 시도 - user={}, movieId={}, msg={}", username, movieId, e.getMessage());
            throw e;
        }
        catch (IllegalArgumentException e) {
            // 잘못된 입력/존재하지 않는 리소스 -> WARN
            log.warn("[SVC] 잘못된 요청 - user={}, movieId={}, msg={}", username, movieId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            // 시스템/DB 문제 -> ERROR
            log.error("[SVC] 영화 찜 등록 실패 - user={}, movieId={}", username, movieId, e);
            throw e;
        }
    }

    // 영화 찜 삭제
    public void removeMovieFavorite(String username, Long movieId) {
        log.debug("[SVC] removeMovieFavorite() start - user={}, movieId={}", username, movieId);
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("영화 없음"));

            MovieFavorite favorite = movieFavoriteRepository.findByUserAndMovie(user, movie)
                    .orElseThrow(() -> new IllegalArgumentException("찜하지 않은 영화입니다."));

            movieFavoriteRepository.delete(favorite);

            log.info("[SVC] 영화 찜 삭제 완료 - user={}, movieId={}", username, movieId);
        }
        catch (IllegalArgumentException e) {
            // 비정상 요청(없는데 삭제) 또는 존재X 리소스 → WARN
            log.warn("[SVC] 잘못된 삭제 요청 - user={}, movieId={}, msg={}", username, movieId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 영화 찜 삭제 실패 - user={}, movieId={}", username, movieId, e);
            throw e;
        }
    }

    // 영화관 찜 추가
    public void addTheaterFavorite(String username, Long theaterId) {
        log.debug("[SVC] addTheaterFavorite() start - user={}, theaterId={}", username, theaterId);
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Theater theater = theaterRepository.findById(theaterId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화관입니다."));

            theaterFavoriteRepository.findByUserAndTheater(user, theater)
                    .ifPresent(f -> { throw new IllegalStateException("이미 찜한 영화관입니다."); });

            theaterFavoriteRepository.save(
                    TheaterFavorite.builder().user(user).theater(theater).build()
            );

            log.info("[SVC] 영화관 찜 등록 완료 - user={}, theaterId={}", username, theaterId);
        } catch (IllegalStateException e) {
            log.warn("[SVC] 중복 찜 시도(영화관) - user={}, theaterId={}, msg={}", username, theaterId, e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(영화관) - user={}, theaterId={}, msg={}", username, theaterId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[SVC] 영화관 찜 등록 실패 - user={}, theaterId={}", username, theaterId, e);
            throw e;
        }
    }

    // 영화관 찜 삭제
    public void removeTheaterFavorite(String username, Long theaterId) {
        log.debug("[SVC] removeTheaterFavorite() start - user={}, theaterId={}", username, theaterId);
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Theater theater = theaterRepository.findById(theaterId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화관입니다."));

            TheaterFavorite fav = theaterFavoriteRepository.findByUserAndTheater(user, theater)
                    .orElseThrow(() -> new IllegalArgumentException("찜하지 않은 영화관입니다."));

            theaterFavoriteRepository.delete(fav);

            log.info("[SVC] 영화관 찜 삭제 완료 - user={}, theaterId={}", username, theaterId);
        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 삭제 요청(영화관) - user={}, theaterId={}, msg={}", username, theaterId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 영화관 찜 삭제 실패 - user={}, theaterId={}", username, theaterId, e);
            throw e;
        }
    }
}
