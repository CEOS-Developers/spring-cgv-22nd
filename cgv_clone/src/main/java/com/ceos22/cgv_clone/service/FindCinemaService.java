package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.CinemaDto;
import com.ceos22.cgv_clone.domain.dto.MovieDto;
import com.ceos22.cgv_clone.domain.reservationMovie.Cinema;
import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import com.ceos22.cgv_clone.repository.CinemaRepository;
import com.ceos22.cgv_clone.repository.MovieRepository;
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
    public CinemaDto getById(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("영화관을 찾을 수 없습니다. id=" + id));
        return CinemaDto.from(cinema);
    }

    /** 전체 페이징 조회 */
    public Page<CinemaDto> getPage(Pageable pageable) {
        return cinemaRepository.findAll(pageable)
                .map(CinemaDto::from);
    }

    /** 이름 검색 (페이징) */
    public Page<CinemaDto> searchByName(String keyword, Pageable pageable) {
        return cinemaRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(CinemaDto::from);
    }
}
