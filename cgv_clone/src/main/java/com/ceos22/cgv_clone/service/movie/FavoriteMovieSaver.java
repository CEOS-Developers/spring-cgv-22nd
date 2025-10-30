package com.ceos22.cgv_clone.service.movie;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;
import com.ceos22.cgv_clone.repository.FavoriteMovieRepository;
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