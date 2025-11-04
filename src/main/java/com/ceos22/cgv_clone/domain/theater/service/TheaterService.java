package com.ceos22.cgv_clone.domain.theater.service;

import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    // 전체 극장 조회
    @Transactional(readOnly = true)
    public List<TheaterResponseDto> getAllTheaters() {
        List<TheaterResponseDto> result = theaterRepository.findAll().stream()
                .map(TheaterResponseDto::fromEntity)
                .toList();
        return result;
    }

    // 단건 극장 조회
    @Transactional(readOnly = true)
    public TheaterResponseDto getTheaterById(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.THEATER_NOT_FOUND, "theaterId=%d인 극장을 찾을 수 없습니다.".formatted(theaterId)));

        return TheaterResponseDto.fromEntity(theater);

    }
}
