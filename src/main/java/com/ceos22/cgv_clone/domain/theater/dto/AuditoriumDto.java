package com.ceos22.cgv_clone.domain.theater.dto;

import com.ceos22.cgv_clone.domain.theater.entity.Auditorium;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuditoriumDto {

    private final String name;
    private final String description;
    private final boolean isSpecial;

    // entity -> dto
    public static AuditoriumDto from(Auditorium auditorium) {
        return new AuditoriumDto(
                auditorium.getName(),
                auditorium.getDescription(),
                auditorium.getAuditoriumType().getIsSpecial()
        );
    }
}
