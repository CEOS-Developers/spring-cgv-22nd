package com.ceos22.cgv_clone.domain.like.service;

import com.ceos22.cgv_clone.domain.like.dto.LikeToggleResponseDto;
import com.ceos22.cgv_clone.domain.like.entity.CinemaLike;
import com.ceos22.cgv_clone.domain.like.entity.MovieLike;
import com.ceos22.cgv_clone.domain.like.repository.CinemaLikeRepository;
import com.ceos22.cgv_clone.domain.like.repository.MovieLikeRepository;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import com.ceos22.cgv_clone.domain.theater.repository.CinemaRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final MovieLikeRepository movieLikeRepository;
    private final CinemaLikeRepository cinemaLikeRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final CinemaRepository cinemaRepository;

    // 영화 찜
    @Transactional
    public LikeToggleResponseDto toggleMovieLike(String email, Long movieId) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Optional<MovieLike> movieLikeOptional = movieLikeRepository.findByMemberIdAndMovieId(member.getId(), movieId);

        boolean isLiked;

        // 이미 좋아요 한 경우
        if (movieLikeOptional.isPresent()) {
            movieLikeRepository.delete(movieLikeOptional.get());
            isLiked = false;
        }
        else {
            Movie movie = movieRepository.findById(movieId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
            movieLikeRepository.save(MovieLike.create(member, movie));
            isLiked = true;
        }

        long likeCount = movieLikeRepository.countByMovieId(movieId);
        return new LikeToggleResponseDto(isLiked, likeCount);
    }

    // 영화관 찜
    @Transactional
    public LikeToggleResponseDto toggleCinemaLike(String email, Long cinemaId) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        Optional<CinemaLike> cinemaLikeOptional = cinemaLikeRepository.findByMemberIdAndCinemaId(member.getId(), cinemaId);

        boolean isLiked;

        if (cinemaLikeOptional.isPresent()) {
            cinemaLikeRepository.delete(cinemaLikeOptional.get());
            isLiked = false;
        } else {
            Cinema cinema = cinemaRepository.findById(cinemaId)
                    .orElseThrow(() -> new IllegalArgumentException("Cinema not found"));
            cinemaLikeRepository.save(CinemaLike.create(member, cinema));
            isLiked = true;
        }

        long likeCount = cinemaLikeRepository.countByCinemaId(cinemaId);
        return new LikeToggleResponseDto(isLiked, likeCount);
    }
}
