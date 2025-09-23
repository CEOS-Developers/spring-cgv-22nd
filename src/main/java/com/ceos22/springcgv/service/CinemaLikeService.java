package com.ceos22.springcgv.service;

import com.ceos22.springcgv.domain.Cinema;
import com.ceos22.springcgv.domain.CinemaLike;
import com.ceos22.springcgv.domain.User;
import com.ceos22.springcgv.repository.CinemaLikeRepository;
import com.ceos22.springcgv.repository.CinemaRepository;
import com.ceos22.springcgv.repository.UserRepository;
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
        User user = userRepository.findById(userId).orElseThrow();
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow();

        // 중복 확인
        if (cinemaLikeRepository.findByUserAndCinema(user, cinema).isPresent()) {
            return;
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
        User user = userRepository.findById(userId).orElseThrow();
        Cinema cinema = cinemaRepository.findById(cinemaId).orElseThrow();

        CinemaLike cinemaLike = cinemaLikeRepository.findByUserAndCinema(user, cinema)
                .orElseThrow(() -> new IllegalArgumentException("찜한 내역이 없습니다."));

        cinemaLikeRepository.delete(cinemaLike);
    }
}