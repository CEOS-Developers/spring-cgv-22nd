package com.ceos22.cgv.module.movie.service;

import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.cinema.domain.CinemaLike;
import com.ceos22.cgv.module.cinema.dto.CinemaLikeResponse;
import com.ceos22.cgv.module.cinema.repository.TheaterRepository;
import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.movie.domain.MovieLike;
import com.ceos22.cgv.module.movie.domain.Schedule;
import com.ceos22.cgv.module.movie.dto.*;
import com.ceos22.cgv.module.movie.repository.MovieLikeRepository;
import com.ceos22.cgv.module.movie.repository.MovieRepository;
import com.ceos22.cgv.module.movie.repository.ScheduleRepository;
import com.ceos22.cgv.module.reservation.domain.ReservationSeat;
import com.ceos22.cgv.module.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final ScheduleRepository scheduleRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MovieListResponse findMovieTitleList(String query) {
        if (query == null || query.isEmpty())  query = "";
        List<Movie> movies = movieRepository.findByQuery(query);
        return MovieListResponse.fromMovieSummaries(movies);

    }

    @Transactional(readOnly = true)
    public MovieResponse findMovieById(Long movieId) {
        var movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Movie not found: " + movieId));
        return MovieResponse.fromMovie(movie);
    }

    @Transactional(readOnly = true)
    public MovieScheduleListResponse findMovieSchedule(ScheduleRequest request) {

        // 조건에 맞는 모든 상영 일정 조회
        List<Schedule> schedules = scheduleRepository.findForSearch(request.movieId(), request.cinemaId(), request.date(), request.category());

        // schdule 표현 형식에 맞게 dto 구성
        List<ScheduleResponse> scheduleResponses = schedules.stream()
                .map(s -> {
                    int totalSeat = s.getTheater().getRow() * s.getTheater().getColumn();
                    long reserved = reservationSeatRepository.countByScheduleId(s.getId());
                    int available = Math.max(0, totalSeat - (int) reserved);
                    return ScheduleResponse.fromScheduleAndAvailability(s, available, totalSeat);
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


        return MovieScheduleListResponse.fromMovieSchedules(groupedMovieSchedule);
    }

    @Transactional
    public MovieLikeResponse like(Long movieId, User authenticatedUser) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 영화가 존재하지 않습니다."));

        if (!movieLikeRepository.existsByUser_IdAndMovie_Id(authenticatedUser.getId(), movieId)) {
            MovieLike like = MovieLike.builder()
                    .user(authenticatedUser)
                    .movie(movie)
                    .build();
            movieLikeRepository.save(like);
        }

        return MovieLikeResponse.fromMovieAndUser(movie, authenticatedUser, true);
    }


    @Transactional
    public MovieLikeResponse unlike(Long movieId, User user) {

        MovieLike movieLike = movieLikeRepository.findByUser_IdAndMovie_Id(user.getId(), movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 좋아요가 존재하지 않습니다."));

        movieLikeRepository.delete(movieLike);

        return new MovieLikeResponse(movieId, user.getId(), false);
    }
}