package com.ceos22.springcgv.service.cinema;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.cinema.CinemaLike;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import com.ceos22.springcgv.repository.cinema.CinemaLikeRepository;
import com.ceos22.springcgv.repository.cinema.CinemaRepository;
import com.ceos22.springcgv.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CinemaLikeService {

    private final CinemaLikeRepository cinemaLikeRepository;
    private final UserRepository userRepository;
    private final CinemaRepository cinemaRepository;

    /**
     * 영화관 찜 등록
     */
    @Transactional
    public void addLike(Long userId, Long cinemaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        // 중복 확인
        if (cinemaLikeRepository.findByUserAndCinema(user, cinema).isPresent()) {
            throw new CustomException(ErrorCode.CINEMA_LIKE_ALREADY_EXISTS);
        }

        CinemaLike newLike = CinemaLike.builder()
                .user(user)
                .cinema(cinema)
                .build();
        cinemaLikeRepository.save(newLike);
    }

    /**
     * 영화관 찜 취소
     */
    @Transactional
    public void removeLike(Long userId, Long cinemaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        CinemaLike cinemaLike = cinemaLikeRepository.findByUserAndCinema(user, cinema)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_LIKE_NOT_FOUND));

        cinemaLikeRepository.delete(cinemaLike);
    }

    /**
     * 영화관 찜 여부 확인
     */
    public boolean isLiked(Long userId, Long cinemaId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        return cinemaLikeRepository.findByUserAndCinema(user, cinema).isPresent();
    }
}