package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinema;
import com.ceos22.cgv_clone.domain.dto.FavoriteCinemaDto;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.Cinema;
import com.ceos22.cgv_clone.repository.CinemaRepository;
import com.ceos22.cgv_clone.repository.FavoriteCinemaRepository;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteCinemaService {

    private final FavoriteCinemaRepository favoriteCinemaRepository;
    private final MemberRepository memberRepository;
    private final CinemaRepository cinemaRepository;

    @Transactional
    public FavoriteCinemaDto add(Long memberId, Long cinemaId) {
        if (favoriteCinemaRepository.existsByMember_IdAndCinema_Id(memberId, cinemaId)) {
            throw new IllegalStateException("이미 찜한 영화관입니다.");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + memberId));
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new IllegalArgumentException("영화관 없음: " + cinemaId));

        FavoriteCinema saved = favoriteCinemaRepository.save(new FavoriteCinema(member, cinema));
        return FavoriteCinemaDto.from(saved);
    }

    @Transactional
    public void remove(Long memberId, Long cinemaId) {
        favoriteCinemaRepository.findByMember_IdAndCinema_Id(memberId, cinemaId)
                .ifPresent(favoriteCinemaRepository::delete);
    }

    @Transactional
    public boolean toggle(Long memberId, Long cinemaId) {
        return favoriteCinemaRepository.findByMember_IdAndCinema_Id(memberId, cinemaId)
                .map(fc -> { favoriteCinemaRepository.delete(fc); return false; }) // 해제
                .orElseGet(() -> { add(memberId, cinemaId); return true; }); // 추가
    }

    public List<FavoriteCinemaDto> list(Long memberId) {
        return favoriteCinemaRepository.findByMember_Id(memberId)
                .stream()
                .map(FavoriteCinemaDto::from)
                .toList();
    }
}