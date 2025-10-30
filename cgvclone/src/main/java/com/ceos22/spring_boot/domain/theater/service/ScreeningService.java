package com.ceos22.spring_boot.domain.theater.service;

import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.movie.repository.MovieRepository;
import com.ceos22.spring_boot.domain.movie.entity.Movie;
import com.ceos22.spring_boot.domain.theater.dto.ScreeningRequestDto;
import com.ceos22.spring_boot.domain.theater.entity.Screen;
import com.ceos22.spring_boot.domain.theater.entity.Screening;
import com.ceos22.spring_boot.domain.theater.entity.ScreeningSeat;
import com.ceos22.spring_boot.domain.theater.entity.Seat;
import com.ceos22.spring_boot.domain.theater.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ScreeningSeatRepository screeningSeatRepository;

    @Transactional
    public Screening createScreening(ScreeningRequestDto request) {

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND, "영화 정보를 찾을 수 없습니다."));
        Screen screen = screenRepository.findById(request.screenId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND, "상영관 정보를 찾을 수 없습니다."));

        Screening screening = new Screening(
                null,
                request.startTime(),
                request.endTime(),
                request.language(),
                movie,
                screen
        );
        Screening saved = screeningRepository.save(screening);

        List<Seat> seats = seatRepository.findByScreen(screen);
        if (seats.isEmpty()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST, "해당 상영관에 좌석이 등록되어 있지 않습니다.");
        }

        List<ScreeningSeat> screeningSeats = seats.stream()
                .map(seat -> new ScreeningSeat(
                        null,
                        saved,
                        seat,
                        seat.isSpecial() ? 15000 : 13000
                ))
                .toList();

        screeningSeatRepository.saveAll(screeningSeats);

        return saved;
    }
}
