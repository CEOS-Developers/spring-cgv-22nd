package com.ceos22.cgv_clone.domains.dibsOn.service;

import com.ceos22.cgv_clone.api.dto.FavoriteCinema;
import com.ceos22.cgv_clone.domains.dibsOn.adaptor.FavoriteCinemaReader;
import com.ceos22.cgv_clone.domains.dibsOn.adaptor.FavoriteCinemaSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteCinemaService {

    private FavoriteCinemaReader favoriteCinemaReader;
    private final FavoriteCinemaSaver favoriteCinemaSaver;

    @Transactional
    public boolean toggle(Long memberId, Long cinemaId) {
        if (favoriteCinemaReader.findFavoriteCinemaStateByMemberId(memberId, cinemaId)) {
            favoriteCinemaSaver.deleteFavoriteMovie(memberId, cinemaId); return false;
        } else {
            favoriteCinemaSaver.saveFavoriteCinema(memberId, cinemaId); return true;
        }
    }

    public List<FavoriteCinema> list(Long memberId) {
        return favoriteCinemaReader.getFavoriteCinemaById(memberId)
                .stream()
                .map(FavoriteCinema::from)
                .toList();
    }
}