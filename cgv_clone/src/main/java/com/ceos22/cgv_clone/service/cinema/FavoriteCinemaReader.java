package com.ceos22.cgv_clone.service.cinema;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinemaEntity;
import com.ceos22.cgv_clone.repository.FavoriteCinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteCinemaReader {
    private final FavoriteCinemaRepository favoriteCinemaRepository;

    /** 찜한 영화관 리스트 조회 */
    public List<FavoriteCinemaEntity> getFavoriteCinemaById(Long memberId) {
        return favoriteCinemaRepository.findByMemberId(memberId);
    }

    /** 영화관 찜 여부 조회 */
    public boolean findFavoriteCinemaStateByMemberId(Long memberId, Long cinemaId) {
        return favoriteCinemaRepository.findByMemberIdAndCinemaId(memberId, cinemaId).isPresent();
    }
}
