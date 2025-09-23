package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.Theater;
import com.ceos22.cgv_clone.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;

    // 극장 전체 조회
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    // 극장 상세 조회
    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id).orElse(null);
    }
}
