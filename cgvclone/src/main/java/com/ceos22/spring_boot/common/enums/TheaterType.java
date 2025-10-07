package com.ceos22.spring_boot.common.enums;

public enum TheaterType {
    IMAX("IMAX"),
    FOUR_DX("4DX"),
    SCREEN_X("ScreenX"),
    DOLBY_CINEMA("Dolby Cinema"),
    PREMIUM("프리미엄관"),
    NORMAL("일반관");

    private final String description;

    TheaterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
