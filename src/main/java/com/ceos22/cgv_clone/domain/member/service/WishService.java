package com.ceos22.cgv_clone.domain.member.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.dto.response.MovieWishResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.TheaterWishResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.MovieWish;
import com.ceos22.cgv_clone.domain.member.entity.TheaterWish;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.member.repository.MovieWishRepository;
import com.ceos22.cgv_clone.domain.member.repository.TheaterWishRepository;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishService {

    private final MovieWishRepository movieWishRepository;
    private final TheaterWishRepository theaterWishRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @Transactional
    public MovieWishResponse addMovieWish(Long memberId, Long movieId) {
        Member member = findMemberById(memberId);
        Movie movie = findMovieById(movieId);

        validateDuplicateMovieWish(member, movie);

        MovieWish movieWish = MovieWish.of(member, movie);
        MovieWish savedMovieWish = movieWishRepository.save(movieWish);

        return MovieWishResponse.from(savedMovieWish);
    }

    @Transactional
    public void removeMovieWish(Long memberId, Long movieId) {
        Member member = findMemberById(memberId);
        Movie movie = findMovieById(movieId);

        MovieWish movieWish = findMovieWish(member, movie);

        movieWishRepository.delete(movieWish);
    }

    public List<MovieWishResponse> getMovieWishes(Long memberId) {
        Member member = findMemberById(memberId);

        List<MovieWish> movieWishes = movieWishRepository.findByMember(member);

        return movieWishes.stream()
                .map(MovieWishResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TheaterWishResponse addTheaterWish(Long memberId, Long theaterId) {
        Member member = findMemberById(memberId);
        Theater theater = findTheaterById(theaterId);

        validateDuplicateTheaterWish(member, theater);

        TheaterWish theaterWish = TheaterWish.of(member, theater);
        TheaterWish savedTheaterWish = theaterWishRepository.save(theaterWish);

        return TheaterWishResponse.from(savedTheaterWish);
    }

    @Transactional
    public void removeTheaterWish(Long memberId, Long theaterId) {
        Member member = findMemberById(memberId);
        Theater theater = findTheaterById(theaterId);

        TheaterWish theaterWish = findTheaterWish(member, theater);

        theaterWishRepository.delete(theaterWish);
    }

    public List<TheaterWishResponse> getTheaterWishes(Long memberId) {
        Member member = findMemberById(memberId);

        List<TheaterWish> theaterWishes = theaterWishRepository.findByMember(member);

        return theaterWishes.stream()
                .map(TheaterWishResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
    private Movie findMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
    }
    private Theater findTheaterById(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));
    }
    private MovieWish findMovieWish(Member member, Movie movie) {
        return movieWishRepository.findByMemberAndMovie(member, movie)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }
    private TheaterWish findTheaterWish(Member member, Theater theater) {
        return theaterWishRepository.findByMemberAndTheater(member, theater)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private void validateDuplicateMovieWish(Member member, Movie movie) {
        if (movieWishRepository.existsByMemberAndMovie(member, movie)){
            throw new CustomException(ErrorCode.DUPLICATED_RESOURCE);
        }
    }
    private void validateDuplicateTheaterWish(Member member, Theater theater) {
        if (theaterWishRepository.existsByMemberAndTheater(member, theater)){
            throw new CustomException(ErrorCode.DUPLICATED_RESOURCE);
        }
    }
}
