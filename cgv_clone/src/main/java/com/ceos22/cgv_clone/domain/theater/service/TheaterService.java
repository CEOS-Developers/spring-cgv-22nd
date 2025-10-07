package com.ceos22.cgv_clone.domain.theater.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.theater.dto.response.ScreenResponse;
import com.ceos22.cgv_clone.domain.theater.entity.Region;
import com.ceos22.cgv_clone.domain.theater.dto.request.ScreenCreateRequest;
import com.ceos22.cgv_clone.domain.theater.dto.request.TheaterCreateRequest;
import com.ceos22.cgv_clone.domain.theater.dto.response.TheaterResponse;
import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.theater.repository.ScreenRepository;
import com.ceos22.cgv_clone.domain.theater.repository.SeatRepository;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;

    public TheaterResponse createTheater(TheaterCreateRequest request) {
        Theater theater = Theater.builder()
                .region(request.region())
                .name(request.name())
                .address(request.address())
                .build();

        Theater savedTheater = theaterRepository.save(theater);
        return TheaterResponse.from(savedTheater);
    }

    public ScreenResponse createScreen(Long theaterId, ScreenCreateRequest request) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        Screen screen = Screen.builder()
                .theater(theater)
                .name(request.name())
                .type(request.type())
                .build();
        screenRepository.save(screen);

        List<Seat> seats = new ArrayList<>();
        for (int i=0; i<=request.totalRows()-1; i++) {
            String rowName = String.valueOf((char) ('A' + i));
            for (int j=1; j<=request.totalCols(); j++) {
                String colNumber = String.valueOf(j);

                Seat seat = Seat.builder()
                        .screen(screen)
                        .rowName(rowName)
                        .columnNumber(colNumber)
                        .build();
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);

        return ScreenResponse.from(screen);
    }

    @Transactional(readOnly = true)
    public List<TheaterResponse> findTheatersByRegion(Region region) {
        List<Theater> theaters = theaterRepository.findByRegion(region);
        return theaters.stream()
                .map(TheaterResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TheaterResponse findTheaterById(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));
        return TheaterResponse.from(theater);
    }
}
