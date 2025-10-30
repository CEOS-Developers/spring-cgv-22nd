package com.ceos22.cgv_clone.domain.theater.dto.response;

import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;

public record ScreenResponse(
        Long screenId,
        String theater,
        String name,
        ScreenType screenType
) {
    public static ScreenResponse from(Screen screen) {
        return new ScreenResponse(
                screen.getScreenId(),
                screen.getTheater().getName(),
                screen.getName(),
                screen.getType());
    }
}
