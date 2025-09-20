package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.dto.CinemaResDto;
import com.ceos22.cgv_clone.web.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public ResponseEntity<List<CinemaResDto.CinemaDto>> getCinemas(){
        List<Cinema> cinemaList = cinemaRepository.findAll();

        List<CinemaResDto.CinemaDto> dtoList = cinemaList.stream()
                .map(CinemaResDto.CinemaDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<CinemaResDto.CinemaDto> getCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(()-> new IllegalArgumentException("Cinema not found with id: " + cinemaId));

        CinemaResDto.CinemaDto cinemaDto = CinemaResDto.CinemaDto.of(cinema);

        return ResponseEntity.ok(cinemaDto);
    }
}
