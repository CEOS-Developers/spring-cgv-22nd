package com.ceos22.cgv_clone.domains.dibsOn.adaptor;

import com.ceos22.cgv_clone.domains.dibsOn.domain.FavoriteMovieEntity;
import com.ceos22.cgv_clone.domains.dibsOn.repository.FavoriteMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteMoiveReader {
    private final FavoriteMovieRepository favoriteMovieRepository;

    /** 찜한 영화 리스트 조회 */
    public List<FavoriteMovieEntity> getFavoriteMovieById(Long memberId) {
        return favoriteMovieRepository.findByMemberId(memberId);
    }

    /** 영화 찜 여부 조회 */
    public boolean findFavoriteMovieStateByMemberId (Long memberId, Long movieId) {
        return favoriteMovieRepository.findByMemberIdAndMovieId(memberId, movieId).isPresent();
    }
}
