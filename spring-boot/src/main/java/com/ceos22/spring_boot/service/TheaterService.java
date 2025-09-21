package com.ceos22.spring_boot.service;

import com.ceos22.spring_boot.entity.Theater;
import com.ceos22.spring_boot.repository.TheaterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;

    // 전체 조회
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    // 단건 조회
    public Theater getTheaterById(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new EntityNotFoundException("Theater not found with id: " + theaterId));
    }
}
