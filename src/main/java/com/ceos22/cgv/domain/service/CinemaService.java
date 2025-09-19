package com.ceos22.cgv.domain.service;

import com.ceos22.cgv.domain.dto.CinemaListResponse;
import com.ceos22.cgv.domain.dto.CinemaResponse;
import com.ceos22.cgv.domain.repository.Cinema;
import com.ceos22.cgv.domain.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    @Transactional(readOnly = true)
    public CinemaListResponse findCinemas(String region) {

        List<Cinema> cinemas;

        if (region == null || region.isEmpty()) {
            cinemas = cinemaRepository.findAll();
        } else {
            cinemas = cinemaRepository.findByRegion(region);
        }

        return CinemaListResponse.fromEntities(cinemas);
    }

    @Transactional(readOnly = true)
    public CinemaResponse findCinemaById(Long cinemaId) {
        var cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cinema not found: " + cinemaId));
        return CinemaResponse.from(cinema);
    }

}
