package com.ceos22.springcgv.service;

import com.ceos22.springcgv.domain.Cinema;
import com.ceos22.springcgv.dto.CinemaDto;
import com.ceos22.springcgv.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    /**
     * 특정 지역의 모든 영화관 목록을 조회하는 메서드
     */
    @Transactional(readOnly = true)
    public List<CinemaDto> findCinemasByRegion(String region) {

        List<Cinema> cinemas = cinemaRepository.findByRegion(region);

        return cinemas.stream()
                .map(CinemaDto::new) // cinema -> new CinemaDto(cinema)
                .collect(Collectors.toList());
    }
}
