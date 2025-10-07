package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovie;
import com.ceos22.cgv_clone.domain.dto.FavoriteCinemaDto;
import com.ceos22.cgv_clone.domain.dto.FavoriteMovieDto;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import com.ceos22.cgv_clone.repository.FavoriteMovieRepository;
import com.ceos22.cgv_clone.repository.MemberRepository;
import com.ceos22.cgv_clone.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteMovieService {

    private final FavoriteMovieRepository favoriteMovieRepository;
    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public FavoriteMovieDto add(Long memberId, Long movieId) {
        if (favoriteMovieRepository.existsByMember_IdAndMovie_Id(memberId, movieId)) {
            throw new IllegalStateException("이미 찜한 영화입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화 없음: " + movieId));

        FavoriteMovie saved = favoriteMovieRepository.save(new FavoriteMovie(member, movie));
        return FavoriteMovieDto.from(saved);
    }

    @Transactional
    public void remove(Long memberId, Long movieId) {
        favoriteMovieRepository.findByMember_IdAndMovie_Id(memberId, movieId)
                .ifPresent(favoriteMovieRepository::delete);
    }

    @Transactional
    public boolean toggle(Long memberId, Long movieId) {
        return favoriteMovieRepository.findByMember_IdAndMovie_Id(memberId, movieId)
                .map(fm -> { favoriteMovieRepository.delete(fm); return false; }) // 해제
                .orElseGet(() -> { add(memberId, movieId); return true; }); // 추가
    }

    public List<FavoriteMovieDto> list(Long memberId) {
        return favoriteMovieRepository.findByMember_Id(memberId)
                .stream()
                .map(FavoriteMovieDto::from)
                .toList();
    }
}