package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.movie.entity.Movie;
import com.ceos22.cgv_clone.domain.movie.repository.MovieRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.request.ScheduleCreateRequest;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ScheduleResponse;
import com.ceos22.cgv_clone.domain.reservation.dto.response.ScheduleSeatResponse;
import com.ceos22.cgv_clone.domain.reservation.entity.Reservation;
import com.ceos22.cgv_clone.domain.reservation.entity.Schedule;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.ScheduleRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import com.ceos22.cgv_clone.domain.theater.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ReservationRepository reservationRepository;

    public ScheduleResponse createSchedule(ScheduleCreateRequest request) {
        // 제약사항 4: 영화 및 상영관 존재 여부 확인
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        Screen screen = screenRepository.findById(request.screenId())
                .orElseThrow(() -> new CustomException(ErrorCode.SCREEN_NOT_FOUND));

        if (request.startTime().isAfter(request.endTime())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        long duration = Duration.between(request.startTime(), request.endTime()).toMinutes();
        if (duration != movie.getRunningTime()) {
            throw new CustomException(ErrorCode.INVALID_RUNNING_TIME);
        }

        if (scheduleRepository.existsByScreenAndStartTimeBetween(screen, request.startTime(), request.endTime())) {
            throw new CustomException(ErrorCode.SCHEDULE_CONFLICT);
        }

        Schedule schedule = Schedule.builder()
                .movie(movie)
                .screen(screen)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.from(savedSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findSchedules(Long movieId, Long theaterId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<Schedule> schedules = scheduleRepository.findSchedulesByCriteria(movieId, theaterId, startOfDay, endOfDay);

        return schedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleSeatResponse findScheduleWithSeatStatus(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        List<Reservation> reservations = reservationRepository.findBySchedule(schedule);
        Set<Long> reservedSeatIds = reservations.stream()
                .map(reservation -> reservation.getSeat().getSeatId())
                .collect(Collectors.toSet());

        return ScheduleSeatResponse.from(schedule, reservedSeatIds);
    }
}