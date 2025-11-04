package com.ceos22.cgv_clone.domains.dibsOn.adaptor;

import com.ceos22.cgv_clone.domains.dibsOn.dto.Cinema;
import com.ceos22.cgv_clone.domains.reservationMovie.domain.CinemaEntity;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.CinemaRepository;
import com.ceos22.cgv_clone.domains.dibsOn.repository.FavoriteCinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CinemaReader {

    private final CinemaRepository cinemaRepository;
    private final FavoriteCinemaRepository favoriteCinemaRepository;

    /** 영화관 단건 조회 */
    public Cinema findById(Long movieId) {
        CinemaEntity cinema = cinemaRepository.findById(movieId)
                .orElseThrow(null);
        return Cinema.from(cinema);
    }
}