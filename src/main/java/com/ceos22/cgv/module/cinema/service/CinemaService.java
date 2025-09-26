package com.ceos22.cgv.module.cinema.service;

import com.ceos22.cgv.module.cinema.domain.CinemaLike;
import com.ceos22.cgv.module.cinema.dto.CinemaLikeResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaRequest;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.cinema.repository.CinemaLikeRepository;
import com.ceos22.cgv.module.cinema.repository.CinemaRepository;
import com.ceos22.cgv.module.movie.domain.Movie;
import com.ceos22.cgv.module.movie.domain.MovieLike;
import com.ceos22.cgv.module.movie.dto.MovieLikeResponse;
import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.module.user.repository.UserRepository;
import com.ceos22.cgv.util.Region;
import com.ceos22.cgv.util.TheaterType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RequiredArgsConstructor
@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final UserRepository userRepository;
    private final CinemaLikeRepository cinemaLikeRepository;

    @Transactional(readOnly = true)
    public CinemaListResponse findCinemas(CinemaRequest cinemaRequest) {
        return CinemaListResponse.fromEntities(cinemaRepository.search(cinemaRequest.region(), cinemaRequest.type()));
    }

    @Transactional(readOnly = true)
    public CinemaResponse findCinemaById(Long cinemaId) {
        var cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cinema not found: " + cinemaId));
        return CinemaResponse.from(cinema);
    }

    @Transactional
    public CinemaLikeResponse like(Long cinemaId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다."));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 영화가 존재하지 않습니다."));


        if (!cinemaLikeRepository.existsByUser_IdAndCinema_Id(userId, cinemaId)) {
            CinemaLike like = CinemaLike.builder()
                    .user(user)
                    .cinema(cinema)
                    .build();
            cinemaLikeRepository.save(like);
        }

        return CinemaLikeResponse.of(cinema, user, true);
    }

    @Transactional
    public CinemaLikeResponse unlike(Long cinemaId, Long userId) {

        cinemaLikeRepository.findByUser_IdAndCinema_Id(userId, cinemaId)
                .ifPresent(cinemaLikeRepository::delete);

        return new CinemaLikeResponse(cinemaId, userId, false);
    }

}
