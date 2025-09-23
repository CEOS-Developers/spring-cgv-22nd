package com.ceos22.spring_boot.dto;

import com.ceos22.spring_boot.common.enums.Region;
import com.ceos22.spring_boot.entity.Theater;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TheaterDto {
    private Long theaterId;
    private String theaterName;
    private Region region;
    private String address;
    private String contact;

    // Entity → DTO 변환
    public static TheaterDto fromEntity(Theater theater) {
        return TheaterDto.builder()
                .theaterId(theater.getTheaterId())
                .theaterName(theater.getTheaterName())
                .region(theater.getRegion())
                .address(theater.getAddress())
                .contact(theater.getContact())
                .build();
    }
}
