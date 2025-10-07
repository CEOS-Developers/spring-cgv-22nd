package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.theater.Theater;
import com.ceos22.cgv_clone.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    // 전체 극장 조회
    public List<TheaterResponseDto> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(TheaterResponseDto::fromEntity)  //TheaterResponseDto의 fromEntity 메소드 이용
                .toList();
    }

    // 단건 극장 조회
    public TheaterResponseDto getTheaterById(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new IllegalArgumentException("영화관을 찾을 수 없음"));

        return TheaterResponseDto.fromEntity(theater);
    }
}
