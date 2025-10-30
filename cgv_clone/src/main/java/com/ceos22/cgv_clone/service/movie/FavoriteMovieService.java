package com.ceos22.cgv_clone.service.movie;

import com.ceos22.cgv_clone.api.dto.FavoriteMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteMovieService {

    private FavoriteMoiveReader favoriteMovieReader;
    private final FavoriteMovieSaver favoriteMovieSaver;

    @Transactional
    public boolean toggle(Long memberId, Long movieId) {
        if (favoriteMovieReader.findFavoriteMovieStateByMemberId(memberId, movieId)) {
            favoriteMovieSaver.deleteFavoriteMovie(memberId, movieId); return false;
        } else {
            favoriteMovieSaver.saveFavoriteMovie(memberId, movieId); return true;
        }
    }

    public List<FavoriteMovie> list(Long memberId) {
        return favoriteMovieReader.getFavoriteMovieById(memberId)
                .stream()
                .map(FavoriteMovie::from)
                .toList();
    }
}