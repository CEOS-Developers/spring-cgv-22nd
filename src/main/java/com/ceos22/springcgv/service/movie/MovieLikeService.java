package com.ceos22.springcgv.service.movie;

import com.ceos22.springcgv.domain.movie.Movie;
import com.ceos22.springcgv.domain.movie.MovieLike;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.repository.movie.MovieLikeRepository;
import com.ceos22.springcgv.repository.movie.MovieRepository;
import com.ceos22.springcgv.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieLikeService {

    private final MovieLikeRepository movieLikeRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    /**
     * 영화 찜 등록
     */
    @Transactional
    public void addLike(Long userId, Long movieId) {
        User user = userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        if (movieLikeRepository.findByUserAndMovie(user, movie).isPresent()) {
            return;
        }

        MovieLike newLike = MovieLike.builder()
                .user(user)
                .movie(movie)
                .build();

        movieLikeRepository.save(newLike);
    }

    /**
     * 영화 찜 취소
     */
    @Transactional
    public void removeLike(Long userId, Long movieId) {
        User user = userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();

        MovieLike movieLike = movieLikeRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new IllegalArgumentException("찜한 내역이 없습니다."));

        movieLikeRepository.delete(movieLike);
    }
}