package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.web.domain.Cinema;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.Theater;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import com.ceos22.cgv_clone.web.dto.CinemaResDto;
import com.ceos22.cgv_clone.web.dto.ScheduleResDto;
import com.ceos22.cgv_clone.web.dto.TheaterResDto;
import com.ceos22.cgv_clone.web.repository.CinemaRepository;
import com.ceos22.cgv_clone.web.repository.ScheduleRepository;
import com.ceos22.cgv_clone.web.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final TheaterRepository theaterRepository;
    private final ScheduleRepository scheduleRepository;

    public ResponseEntity<List<CinemaResDto.CinemaDto>> getCinemas(Region region) {

        List<Cinema> cinemaList = null;

        if (region == null) {
            cinemaList = cinemaRepository.findAll();
        } else {
            cinemaList = cinemaRepository.findByRegion(region);
        }

        List<CinemaResDto.CinemaDto> dtoList = cinemaList.stream()
                .map(CinemaResDto.CinemaDto::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<CinemaResDto.CinemaDetailDto> getCinema(Long cinemaId) {

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(()-> new IllegalArgumentException("Cinema not found with id: " + cinemaId));

        List<Theater> theaterList = theaterRepository.findByCinemaId(cinemaId);

        List<TheaterResDto.TheaterDto> theaterDtos = theaterList.stream()
                .map(thater-> {
                    List<Schedule> schedules = scheduleRepository.findByTheaterId(thater.getId());
                    List<ScheduleResDto.ScheduleDto> scheduleDtos = schedules.stream()
                            .map(ScheduleResDto.ScheduleDto::of)
                            .toList();
                    return TheaterResDto.TheaterDto.of(thater,scheduleDtos);
                })
                .toList();

        CinemaResDto.CinemaDetailDto cinemaDetailDto = CinemaResDto.CinemaDetailDto.of(cinema,theaterDtos);
        return ResponseEntity.ok(cinemaDetailDto);
    }
}
