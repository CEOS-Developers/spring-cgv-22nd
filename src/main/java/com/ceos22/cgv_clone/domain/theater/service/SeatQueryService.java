package com.ceos22.cgv_clone.domain.theater.service;
import com.ceos22.cgv_clone.domain.booking.repository.BookingSeatRepository;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import com.ceos22.cgv_clone.domain.screening.repository.ScreeningRepository;
import com.ceos22.cgv_clone.domain.theater.dto.SeatStatusDto;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeatQueryService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Transactional(readOnly = true)
    public List<SeatStatusDto> getSeatMap(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCREENING_NOT_FOUND));

        Long auditoriumId = screening.getAuditorium().getId();
//        List<Seat> seats = seatRepository.findAllByAuditoriumIdSorted(auditoriumId);
//
//        Set<Long> reservedSeatIds = new HashSet<>(
//                bookingSeatRepository.findByScreeningId(screeningId)
//                        .stream().map(bs -> bs.getSeat().getId()).toList()
//        );
//
//        return seats.stream()
//                .map(s -> new SeatStatusDto(
//                        s.getId(), s.getRowNo(), s.getColumnNo(),
//                        reservedSeatIds.contains(s.getId())
//                ))
//                .toList();

        // 단일 쿼리 호출로 변경
        return seatRepository.findSeatStatusByAuditoriumAndScreening(auditoriumId, screeningId);
    }
}
