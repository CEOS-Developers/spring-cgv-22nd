package com.ceos22.cgv_clone.service.cinema;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteCinemaEntity;
import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;
import com.ceos22.cgv_clone.repository.FavoriteCinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCinemaSaver {
    private final FavoriteCinemaRepository favoriteCinemaRepository;

    /** 찜 저장 */
    public void saveFavoriteCinema(Long memberId, Long cinemaId) {
        favoriteCinemaRepository.save(new FavoriteCinemaEntity(memberId, cinemaId));
    }

    /** 찜 해제*/
    public void deleteFavoriteMovie(Long memberId, Long movieId) {
        favoriteCinemaRepository.delete(new FavoriteCinemaEntity(memberId, movieId));
    }

}