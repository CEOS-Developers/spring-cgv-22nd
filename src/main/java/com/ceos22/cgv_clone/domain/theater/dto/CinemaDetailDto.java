package com.ceos22.cgv_clone.domain.theater.dto;

import com.ceos22.cgv_clone.domain.common.enums.Region;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CinemaDetailDto {

    private final Long cinamaId;
    private final Region region;
    private final String name;
    private final List<AuditoriumDto> auditoriums;

    public static CinemaDetailDto of(Cinema cinema, List<AuditoriumDto> auditoriums) {
        return new CinemaDetailDto(cinema.getId(), cinema.getRegion(), cinema.getName(), auditoriums);
    }
}
