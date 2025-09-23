package com.ceos22.cgv_clone.domain.theater.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.region.entity.Region;
import com.ceos22.cgv_clone.domain.region.repository.RegionRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final RegionRepository regionRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;

    public TheaterResponse createTheater(TheaterCreateRequest request) {
        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new CustomException(ErrorCode.REGION_NOT_FOUND));

        Theater theater = Theater.builder()
                .region(region)
                .name(request.name())
                .address(request.address())
                .build();

        Theater savedTheater = theaterRepository.save(theater);
        return TheaterResponse.from(savedTheater);
    }

    public TheaterResponse createScreen(Long theaterId, ScreenCreateRequest request) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        Screen screen = Screen.builder()
                .theater(theater)
                .name(request.name())
                .type(request.type())
                .build();
        screenRepository.save(screen);

        List<Seat> seats = request.seats().stream()
                .map(seatRequest -> Seat.builder()
                        .screen(screen)
                        .rowName(seatRequest.rowName())
                        .columnNumber(seatRequest.columnNumber())
                        .build())
                .collect(Collectors.toList());
        seatRepository.saveAll(seats);

        theater.getScreens().add(screen);

        return TheaterResponse.from(theater);
    }

    @Transactional(readOnly = true)
    public List<TheaterResponse> findAllTheaters(Long regionId) {
        List<Theater> theaters = theaterRepository.findByRegionRegionId(regionId);
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
