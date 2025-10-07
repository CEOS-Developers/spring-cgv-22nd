package com.ceos22.cgv_clone.domain.theater.dto;

import com.ceos22.cgv_clone.domain.common.enums.Region;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CinemaSummaryDto {

    private final Long cinemaId;
    private final String name;
    private final Region region;

    public static CinemaSummaryDto of(Cinema cinema) {
        return new CinemaSummaryDto(cinema.getId(), cinema.getName(), cinema.getRegion());
    }
}
