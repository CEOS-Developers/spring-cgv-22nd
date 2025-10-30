package com.ceos22.cgv_clone.domain.theater.service;

import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponseDto;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    // 전체 극장 조회
    public List<TheaterResponseDto> getAllTheaters() {
        log.debug("[SVC] [SVC] getAllTheaters start");
        try {
            List<TheaterResponseDto> result = theaterRepository.findAll().stream()
                    .map(TheaterResponseDto::fromEntity)
                    .toList();

            if (result.isEmpty()) {
                log.warn("[SVC] 등록된 극장이 없습니다.");
            }
            else {
                log.info("[SVC] 전체 극장 조회 완료 - {}개", result.size());
            }
            return result;
        }
        catch (Exception e) {
            log.error("[SVC] 전체 극장 조회 실패", e);
            throw e;
        }
    }

    // 단건 극장 조회
    public TheaterResponseDto getTheaterById(Long theaterId) {
        log.debug("[SVC] getTheaterById start - theaterId={}", theaterId);
        try {
            Theater theater = theaterRepository.findById(theaterId)
                    .orElseThrow(() -> new IllegalArgumentException("영화관을 찾을 수 없음"));
            log.info("[SVC] 극장 조회 성공 - id={}, name={}", theater.getId(), theater.getName());
            return TheaterResponseDto.fromEntity(theater);
        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 존재하지 않는 극장 요청 - theaterId={}", theaterId);
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 극장 조회 중 오류 - theaterId={}", theaterId, e);
            throw e;
        }
    }
}
