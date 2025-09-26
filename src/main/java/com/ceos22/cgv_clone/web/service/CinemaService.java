package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.Theater;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.dto.CinemaResponseDto;
import com.ceos22.cgv_clone.web.dto.ScheduleResponseDto;
import com.ceos22.cgv_clone.web.dto.TheaterResponseDto;
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
                .map(thater-> {
                    List<Schedule> schedules = scheduleRepository.findByTheaterId(thater.getId());
                    List<ScheduleResponseDto.ScheduleDto> scheduleDtos = schedules.stream()
                            .map(ScheduleResponseDto.ScheduleDto::of)
                            .toList();
                    return TheaterResponseDto.TheaterDto.of(thater,scheduleDtos);
                })
                .toList();

        CinemaResponseDto.CinemaDetailDto cinemaDetailDto = CinemaResponseDto.CinemaDetailDto.of(cinema,theaterDtos);
        return cinemaDetailDto;
    }
}
