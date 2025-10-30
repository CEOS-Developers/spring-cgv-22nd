package com.ceos22.cgv_clone.domain.movie.service;

import com.ceos22.cgv_clone.domain.movie.dto.request.DirectorCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.DirectorResponse;
import com.ceos22.cgv_clone.domain.movie.entity.Director;
import com.ceos22.cgv_clone.domain.movie.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorResponse createDirector(DirectorCreateRequest request) {
        Director director = Director.builder()
                .name(request.name())
                .birthDate(request.birthDate())
                .profileImageUrl(request.profileImageUrl())
                .build();

        Director savedDirector = directorRepository.save(director);
        return DirectorResponse.from(savedDirector);
    }
}
