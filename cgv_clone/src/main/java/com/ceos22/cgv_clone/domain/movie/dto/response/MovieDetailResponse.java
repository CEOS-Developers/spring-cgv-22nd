package com.ceos22.cgv_clone.domain.movie.dto.response;

import com.ceos22.cgv_clone.domain.movie.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record MovieDetailResponse(
        Long movieId,
        String title,
        Genre genre,
        MovieStatus status,
        Integer runningTime,
        LocalDate releaseDate,
        String posterUrl,
        DirectorSimpleInfo director,
        List<ActorSimpleInfo> actors
) {
    public record ActorSimpleInfo(Long actorId, String name, String profileImageUrl) {
        public static ActorSimpleInfo from(Actor actor) {
            return new ActorSimpleInfo(actor.getActorId(), actor.getName(), actor.getProfileImageUrl());
        }
    }
    public record DirectorSimpleInfo(Long actorId, String name, String profileImageUrl) {
        public static DirectorSimpleInfo from(Director director) {
            return new DirectorSimpleInfo(director.getDirectorId(), director.getName(), director.getProfileImageUrl());
        }
    }
    public static MovieDetailResponse from(Movie movie) {
        DirectorSimpleInfo directorSimpleInfo = DirectorSimpleInfo.from(movie.getDirector());
        List<ActorSimpleInfo> actorSimpleInfos = movie.getActors().stream()
                .map(ActorSimpleInfo::from)
                .collect(Collectors.toList());

        return new MovieDetailResponse(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getStatus(),
                movie.getRunningTime(),
                movie.getReleaseDate(),
                movie.getPosterUrl(),
                directorSimpleInfo,
                actorSimpleInfos
        );
    }
}
