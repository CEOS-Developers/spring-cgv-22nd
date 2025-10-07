package com.ceos22.cgv_clone.domain.screening.service;

import com.ceos22.cgv_clone.domain.screening.dto.ScreeningDto;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import com.ceos22.cgv_clone.domain.screening.repository.ScreeningRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScreeningQueryService {

    private final ScreeningRepository screeningRepository;

    public List<ScreeningDto> findByConditions(
            Long cinemaId, Long auditoriumId, Long movieId, LocalDate date
    ) {

        if (cinemaId == null && auditoriumId == null) {
            throw new CustomException(ErrorCode.VALIDATION_FAILED);
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        List<Screening> list;
        if (auditoriumId != null) {
            list = screeningRepository.findByAuditoriumWithDetails(auditoriumId, start, end);
        } else if (movieId != null) {
            list = screeningRepository.findByCinemaAndMovieWithDetails(
                    cinemaId, movieId, start, end);
        } else {
            list = screeningRepository.findByCinemaWithDetails(
                    cinemaId, start, end);
        }

        return list.stream()
                .map(s -> new ScreeningDto(
                        s.getId(),
                        s.getMovie().getId(),
                        s.getMovie().getTitle(),
                        s.getAuditorium().getId(),
                        s.getAuditorium().getName(),
                        s.getStartedAt(),
                        s.getEndedAt()
                ))
                .toList();
    }
}
