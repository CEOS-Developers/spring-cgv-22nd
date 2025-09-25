package com.ceos22.cgv.module.movie.service;

import com.ceos22.cgv.module.cinema.repository.TheaterRepository;
import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.movie.domain.Schedule;
import com.ceos22.cgv.module.movie.dto.*;
import com.ceos22.cgv.module.movie.repository.MovieRepository;
import com.ceos22.cgv.module.movie.repository.ScheduleRepository;
import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import com.ceos22.cgv.module.reservation.repository.ReservationSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public MovieListResponse findMovies(MovieRequest request) {
        return MovieListResponse.fromEntities(movieRepository.findAll());
    }

    public MovieListResponse findMovieTitleList(String query) {
        List<Movie> movies = movieRepository.findByQuery(query);
        return MovieListResponse.fromSummaryEntities(movies);

    }

    public MovieResponse findMovieById(Long movieId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie not found: " + movieId));
        return MovieResponse.from(movie);
    }

    public MovieScheduleListResponse findMovieSchedule(ScheduleRequest request) {


        if (request.movieId() != null && request.cinemaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cinema id가 필요합니다");
        }
        if (request.movieId() == null && request.cinemaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "movieId 또는 cinemaId 중 하나는 반드시 필요합니다.");
        }

        // 조건에 맞는 모든 상영 일정 조회
        List<Schedule> schedules = scheduleRepository.findForSearch(request.movieId(), request.cinemaId(), request.date(), request.category());

        // schdule 표현 형식에 맞게 dto 구성
        List<ScheduleResponse> scheduleResponses = schedules.stream()
                .map(s -> {
                    int totalSeat = s.getTheater().getRow() * s.getTheater().getColumn();
                    int reserved = reservationSeatRepository.countByScheduleId(s.getId());
                    int available = Math.max(0, totalSeat - (int) reserved);
                    return ScheduleResponse.from(s, available, totalSeat);
                })
                .toList();
        
        // 영화 별로 상영 일정 그룹화
        Map<Movie, List<ScheduleResponse>> groupedMovieSchedule =
                IntStream.range(0, schedules.size())
                        .boxed()
                        .collect(Collectors.groupingBy(
                                i -> schedules.get(i).getMovie(),
                                Collectors.mapping(scheduleResponses::get, Collectors.toList())
                        ));


        return MovieScheduleListResponse.fromGrouped(groupedMovieSchedule);
    }

}
