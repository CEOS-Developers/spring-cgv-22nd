package com.ceos22.cgv_clone.domains.orderFood.adaptor;

import com.ceos22.cgv_clone.domains.reservationMovie.repository.CinemaRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CinemaReader {
    private final CinemaRepository cinemaRepository;

    public String findCinemaName(Long cinemaId) {
        return cinemaRepository.findById(cinemaId).get().getName();
    }

}
