package com.ceos22.cgv_clone.service.cinema;

import com.ceos22.cgv_clone.domain.dto.Cinema;
import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;
import com.ceos22.cgv_clone.repository.CinemaRepository;
import com.ceos22.cgv_clone.repository.FavoriteCinemaRepository;
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