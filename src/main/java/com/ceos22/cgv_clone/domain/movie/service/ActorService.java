package com.ceos22.cgv_clone.domain.movie.service;

import com.ceos22.cgv_clone.domain.movie.dto.request.ActorCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.ActorResponse;
import com.ceos22.cgv_clone.domain.movie.entity.Actor;
import com.ceos22.cgv_clone.domain.movie.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorResponse createActor(ActorCreateRequest request) {
        Actor actor = Actor.builder()
                .name(request.name())
                .birthDate(request.birthDate())
                .profileImageUrl(request.profileImageUrl())
                .build();

        Actor savedActor = actorRepository.save(actor);
        return ActorResponse.from(savedActor);
    }
}
