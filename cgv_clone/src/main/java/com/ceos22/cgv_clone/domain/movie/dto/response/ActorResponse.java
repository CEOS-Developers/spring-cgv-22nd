package com.ceos22.cgv_clone.domain.movie.dto.response;

import com.ceos22.cgv_clone.domain.movie.entity.Actor;

import java.time.LocalDate;

public record ActorResponse(
        Long actorId,
        String name,
        LocalDate birthDate,
        String profileImageUrl
) {
    public static ActorResponse from(Actor actor) {
        return new ActorResponse(
                actor.getActorId(),
                actor.getName(),
                actor.getBirthDate(),
                actor.getProfileImageUrl()
        );
    }
}