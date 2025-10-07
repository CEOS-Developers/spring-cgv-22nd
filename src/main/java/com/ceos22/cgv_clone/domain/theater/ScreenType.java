package com.ceos22.cgv_clone.domain.theater;

public enum ScreenType {
    NORMAL("일반관"),
    SPECIAL("특별관");

    private final String description;

    ScreenType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
