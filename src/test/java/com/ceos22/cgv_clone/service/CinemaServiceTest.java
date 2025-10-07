package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.theater.dto.CinemaDetailDto;
import com.ceos22.cgv_clone.domain.theater.dto.CinemaSummaryDto;
import com.ceos22.cgv_clone.domain.theater.entity.Auditorium;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import com.ceos22.cgv_clone.domain.theater.repository.CinemaRepository;
import com.ceos22.cgv_clone.domain.theater.service.CinemaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @InjectMocks
    private CinemaService cinemaService;

    @Test
    @DisplayName("전체 영화관 목록 조회 - 성공")
    void getCinemaListSuccess() {

        // given
        Cinema cinema1 = new Cinema();
        Cinema cinema2 = new Cinema();
        List<Cinema> cinemas = List.of(cinema1, cinema2);

        given(cinemaRepository.findAll()).willReturn(cinemas);

        // when
        List<CinemaSummaryDto> result = cinemaService.getCinemaList();

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        verify(cinemaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("특정 영화관 상세 정보 조회 - 성공")
    void getCinemaDetailSuccess() {
        // given
        long cinemaId = 1L;
        Cinema cinema = new Cinema();
        Auditorium auditorium1 = new Auditorium();
        Auditorium auditorium2 = new Auditorium();
        cinema.getAuditoriums().addAll(List.of(auditorium1, auditorium2)); // Cinema가 Auditorium 리스트를 가지고 있다고 가정

        given(cinemaRepository.findByIdWithAuditoriums(cinemaId)).willReturn(Optional.of(cinema));

        // when
        CinemaDetailDto result = cinemaService.getCinema(cinemaId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCinamaId()).isEqualTo(cinema.getId());
        assertThat(result.getAuditoriums().size()).isEqualTo(2); // Auditorium 정보가 DTO에 잘 담겼는지 확인
    }
}