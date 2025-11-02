package com.ceos22.cgv_clone.domains.dibsOn.adaptor;

import com.ceos22.cgv_clone.domains.dibsOn.domain.FavoriteMovieEntity;
import com.ceos22.cgv_clone.domains.dibsOn.repository.FavoriteMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteMovieSaver {
    private final FavoriteMovieRepository favoriteMovieRepository;

    /** 찜 저장 */
    public void saveFavoriteMovie(Long memberId, Long movieId) {
        favoriteMovieRepository.save(new FavoriteMovieEntity(memberId, movieId));
    }

    /** 찜 해제*/
    public void deleteFavoriteMovie(Long memberId, Long movieId) {
        favoriteMovieRepository.delete(new FavoriteMovieEntity(memberId, movieId));
    }

}