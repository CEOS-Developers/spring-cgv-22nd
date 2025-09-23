package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.*;
import com.ceos22.cgv_clone.dto.movie.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final PosterRepository posterRepository;
    private final MoviePersonRepository moviePersonRepository;
    private final EventRepository eventRepository;
    private final TrailerRepository trailerRepository;
    private final TheaterRepository theaterRepository;
    private final ShowtimeRepository showtimeRepository;

    //홈 화면 영화 정보
    public List<MovieHomeDto> getHomeMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> MovieHomeDto.builder()
                        .movieId(movie.getId())
                        .title(movie.getTitle())
                        .ageRating(movie.getAgeRating().toString())
                        .bookingRate(movie.getBookingRate())
                        .releaseDate(movie.getReleaseDate())
                        .totalAudience(movie.getTotalAudience())
                        .eggNum(movie.getEggNum())
                        .posterUrl(
                                posterRepository.findByMovieId(movie.getId()).isEmpty() ? null : posterRepository.findByMovieId(movie.getId()).get(0).getImageUrl()
                        )
                        .build()
                ).toList();
    }

    //영화 상세 조회
    public MovieDetailDto getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없음"));

        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        List<Poster> posters = posterRepository.findByMovieId(movieId);
        List<MoviePerson> persons = moviePersonRepository.findByMovieId(movieId);
        List<Event> events = eventRepository.findByMovieId(movieId);
        List<Trailer> trailers = trailerRepository.findByMovieId(movieId);

        return MovieDetailDto.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .ageRating(movie.getAgeRating().toString())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .genre(movie.getGenre())
                .bookingRate(movie.getBookingRate())
                .totalAudience(movie.getTotalAudience())
                .eggNum(movie.getEggNum())
                .prologue(movie.getPrologue())
                .persons(persons.stream()
                        .map(p -> new MoviePersonDto(p.getId(), p.getName(), p.getRoleType(), p.getImageUrl()))
                        .toList())
                .posters(posters.stream()
                        .map(p -> new PosterDto(p.getId(), p.getImageUrl()))
                        .toList())
                .events(events.stream()
                        .map(e -> new EventDto(
                                e.getId(), e.getTitle(), e.getDescription(),
                                e.getStartDate(), e.getEndDate(), e.getImageUrl()
                        ))
                        .toList())
                .reviews(reviews.stream()
                        .map(r -> new ReviewDto(
                                r.getId(), r.getContent(), r.getRating(),
                                r.getCreatedAt(), r.getUser().getUsername()
                        ))
                        .toList())
                .trailers(trailers.stream()
                        .map(t -> new TrailerDto(t.getId(), t.getVideoUrl(), t.getDescription()))
                        .toList())
                .build();
    }

    // 영화별 상영 극장 조회
    public List<Theater> getTheatersByMovie(Long movieId) {
        return theaterRepository.findTheatersByMovieId(movieId);
    }

    // 특정 영화 + 특정 극장의 상영 시간표 조회
    public List<ShowtimeResponseDto> getShowtimes(Long movieId, Long theaterId) {
        List<Showtime> showtimes = showtimeRepository.findByMovie_IdAndScreen_Theater_Id(movieId, theaterId);
        return showtimes.stream()
                .map(s -> ShowtimeResponseDto.builder()
                        .showtimeId(s.getId())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .screenName(s.getScreen().getName())
                        .screenType(s.getScreen().getType())
                        .totalSeats(s.getScreen().getTotalSeats())
                        .build())
                .collect(Collectors.toList());
    }
}
