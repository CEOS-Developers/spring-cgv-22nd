package com.ceos22.cgv_clone.domains.reservationMovie.adaptor;

import com.ceos22.cgv_clone.domains.reservationMovie.domain.ScreeningEntity;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreeningReader {
    private final ScreeningRepository screeningRepository;

    public void getScreeningById(Long id) {
        screeningRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상영관입니다."));
    }
}
