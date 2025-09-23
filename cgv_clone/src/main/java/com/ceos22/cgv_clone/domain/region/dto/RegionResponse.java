package com.ceos22.cgv_clone.domain.region.dto;

import com.ceos22.cgv_clone.domain.region.entity.Region;

public record RegionResponse (
        Long regionId,
        String regionName
){
    public static RegionResponse from(Region region) {
        return new RegionResponse(
                region.getRegionId(),
                region.getRegionName()
        );
    }
}
