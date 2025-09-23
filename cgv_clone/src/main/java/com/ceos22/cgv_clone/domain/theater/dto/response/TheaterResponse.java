package com.ceos22.cgv_clone.domain.theater.dto.response;

import com.ceos22.cgv_clone.domain.theater.entity.Screen;
import com.ceos22.cgv_clone.domain.theater.entity.ScreenType;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;

import java.util.List;
import java.util.stream.Collectors;

public record TheaterResponse(
        Long theaterId,
        String name,
        String address,
        List<ScreenType> screenTypes
) {
    public static TheaterResponse from(Theater theater) {
        List<ScreenType> types = theater.getScreens().stream()
                .map(Screen::getType)
                .distinct()
                .collect(Collectors.toList());

        return new TheaterResponse(
                theater.getTheaterId(),
                theater.getName(),
                theater.getAddress(),
                types
        );
    }
}
