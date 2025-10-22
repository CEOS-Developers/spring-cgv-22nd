package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.domain.Seat;
import com.ceos22.cgv_clone.web.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public List<Seat> getSeatsById(List<Long> ids) {
        List<Seat> seats = seatRepository.findAllById(ids);
        if (seats.size() != ids.size()) {
            throw new GeneralException(ErrorStatus.SEAT_NOT_FOUND);
        }
        return seats;
    }
}
