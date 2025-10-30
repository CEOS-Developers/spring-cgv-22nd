package com.ceos22.spring_boot.domain.theater.service;

import com.ceos22.spring_boot.domain.theater.dto.TheaterDto;
import com.ceos22.spring_boot.domain.theater.entity.Theater;
import com.ceos22.spring_boot.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    // 영화관 전체 조회
    public List<TheaterDto> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(TheaterDto::fromEntity)
                .toList();
    }

    // 특정 영화관 조회
    public TheaterDto getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 영화관입니다. ID=" + id));
        return TheaterDto.fromEntity(theater);
    }
}
