package com.ceos22.cgv.module.cinema.service;

import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.cinema.repository.CinemaRepository;
import com.ceos22.cgv.util.Region;
import com.ceos22.cgv.util.TheaterType;
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
    public CinemaListResponse findCinemas(CinemaRequest cinemaRequest) {
        return CinemaListResponse.fromEntities(cinemaRepository.search(cinemaRequest.region(), cinemaRequest.type()));
    }

    @Transactional(readOnly = true)
    public CinemaResponse findCinemaById(Long cinemaId) {
        var cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cinema not found: " + cinemaId));
        return CinemaResponse.from(cinema);
    }

}
