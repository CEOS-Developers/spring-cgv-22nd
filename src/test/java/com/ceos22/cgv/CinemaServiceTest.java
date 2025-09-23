package com.ceos22.cgv;

import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;
import com.ceos22.cgv.module.cinema.dto.CinemaResponse;
import com.ceos22.cgv.module.cinema.domain.Cinema;
import com.ceos22.cgv.module.cinema.repository.CinemaRepository;
import com.ceos22.cgv.module.cinema.service.CinemaService;
import com.ceos22.cgv.util.TheaterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CinemaServiceTest {

    @Mock
    CinemaRepository cinemaRepository;

    @InjectMocks
    CinemaService cinemaService;

    // region == null 또는 빈문자열이면 findAll 호출
    @ParameterizedTest
    @NullAndEmptySource
    void findCinemas_withoutRegion(String region, TheaterType type) {

        // given
        var cinema = new Cinema();
        when(cinemaRepository.findAll()).thenReturn(List.of(cinema));

        // when
        CinemaListResponse result = cinemaService.findCinemas(region, type);

        // then
        verify(cinemaRepository, times(1)).findAll();
        verify(cinemaRepository, never()).findByRegion(anyString());
        assertThat(result).isNotNull();
    }

    // region 있으면 findByRegion 호출
    @Test
    void findCinemas_withRegion() {

        // given
        String region = "경기";
        TheaterType type = TheaterType.STANDARD;
        var cinema = new Cinema();
        when(cinemaRepository.findByRegion(region)).thenReturn(List.of(cinema));

        // when
        CinemaListResponse result = cinemaService.findCinemas(region, type);

        // then
        verify(cinemaRepository, times(1)).findByRegion(region);
        verify(cinemaRepository, never()).findAll();
        assertThat(result).isNotNull();
    }

    // id를 통한 영화관 조회
    @Test
    void findCinemaById() {

        // given
        Long id = 1L;
        var cinema = new Cinema(/* ... */);
        when(cinemaRepository.findById(id)).thenReturn(Optional.of(cinema));

        // when
        CinemaResponse result = cinemaService.findCinemaById(id);

        // then
        verify(cinemaRepository, times(1)).findById(id);
        assertThat(result).isNotNull();
    }

}
