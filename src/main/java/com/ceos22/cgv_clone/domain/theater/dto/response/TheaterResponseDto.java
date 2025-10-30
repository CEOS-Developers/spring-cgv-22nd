package com.ceos22.cgv_clone.domain.theater.dto.response;

import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import lombok.*;

@Getter
@Builder
public class TheaterResponseDto {
    private Long theaterId;
    private String name;
    private String address;
    private String acessInfo;
    private String parkingInfo;
    private String region;

    // 엔티티 -> Dto
    public static TheaterResponseDto fromEntity(Theater theater) {
        return TheaterResponseDto.builder()
                .theaterId(theater.getId())
                .name(theater.getName())
                .address(theater.getAddress())
                .acessInfo(theater.getAccessInfo())
                .parkingInfo(theater.getParkingInfo())
                .region(theater.getRegion().getDescription())
                .build();
    }
}
