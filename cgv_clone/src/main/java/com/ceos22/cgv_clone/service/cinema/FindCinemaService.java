package com.ceos22.cgv_clone.service.cinema;

import com.ceos22.cgv_clone.api.dto.Cinema;
import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;
import com.ceos22.cgv_clone.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindCinemaService {
    private final CinemaRepository cinemaRepository;

    /** 단건 조회 */
    public Cinema getById(Long id) {
        CinemaEntity cinemaEntity = cinemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("영화관을 찾을 수 없습니다. id=" + id));
        return Cinema.from(cinemaEntity);
    }

    /** 전체 페이징 조회 */
    public Page<Cinema> getPage(Pageable pageable) {
        return cinemaRepository.findAll(pageable)
                .map(Cinema::from);
    }

    /** 이름 검색 (페이징) */
    public Page<Cinema> searchByName(String keyword, Pageable pageable) {
        return cinemaRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(Cinema::from);
    }
}
