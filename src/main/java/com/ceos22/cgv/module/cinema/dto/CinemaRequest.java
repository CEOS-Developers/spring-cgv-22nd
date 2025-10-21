package com.ceos22.cgv.module.cinema.dto;

import com.ceos22.cgv.common.util.Region;
import com.ceos22.cgv.common.util.TheaterType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

public record CinemaRequest(
        Region region,
        TheaterType type
) {

    @Schema(hidden = true)
    @AssertTrue(message = "region 또는 type 중 하나만 전달하세요.")
    public boolean isOnlyOne() {
        return (region != null) ^ (type != null);
    }

}
