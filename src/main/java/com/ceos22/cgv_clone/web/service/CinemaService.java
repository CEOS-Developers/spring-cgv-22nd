package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.dto.CinemaResponseDto;
import com.ceos22.cgv_clone.web.dto.ScheduleResponseDto;
import com.ceos22.cgv_clone.web.dto.TheaterResponseDto;
import com.ceos22.cgv_clone.web.repository.CinemaPreferRepository;
import com.ceos22.cgv_clone.web.repository.CinemaRepository;
import com.ceos22.cgv_clone.web.repository.ScheduleRepository;
import com.ceos22.cgv_clone.web.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final TheaterRepository theaterRepository;
    private final ScheduleRepository scheduleRepository;
    private final CinemaPreferRepository cinemaPreferRepository;

    @Transactional(readOnly = true)
    public List<CinemaResponseDto.CinemaDto> getCinemas(Region region) {

        List<Cinema> cinemaList = null;

        if (region == null) {
            cinemaList = cinemaRepository.findAll();
        } else {
            cinemaList = cinemaRepository.findByRegion(region);
        }

        List<CinemaResponseDto.CinemaDto> dtoList = cinemaList.stream()
                .map(CinemaResponseDto.CinemaDto::of)
                .collect(Collectors.toList());

        return dtoList;
    }

    public CinemaResponseDto.CinemaDetailDto getCinema(Long cinemaId) {

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(()-> new IllegalArgumentException("Cinema not found with id: " + cinemaId));

        List<Theater> theaterList = theaterRepository.findByCinemaId(cinemaId);

        List<TheaterResponseDto.TheaterDto> theaterDtos = theaterList.stream()
                .map(theater-> {
                    List<ScheduleResponseDto.ScheduleDto> scheduleDtos = scheduleRepository
                            .findByTheaterId(theater.getId()).stream()
                            .filter(Schedule::notStarted)
                            .map(ScheduleResponseDto.ScheduleDto::of)
                            .collect(Collectors.toList());

                    return TheaterResponseDto.TheaterDto.of(theater, scheduleDtos);
                })
                .collect(Collectors.toList());

        return CinemaResponseDto.CinemaDetailDto.of(cinema,theaterDtos);
    }

    public void preferCinema(Long cinemaId, User user) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.CINEMA_NOT_FOUND));

        if (cinemaPreferRepository.existsByUserAndCinema(user,cinema)){
            throw new GeneralException(ErrorStatus.ALREADY_PREFERED_CINEMA);
        }
        CinemaPrefer cinemaPrefer = CinemaPrefer.builder()
                .cinema(cinema)
                .user(user)
                .build();

        cinemaPreferRepository.save(cinemaPrefer);
    }
}
