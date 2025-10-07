package com.ceos22.spring_boot.common.enums;

public enum Rating {
    ALL("전체관람가"),
    AGE_12("12세 이상 관람가"),
    AGE_15("15세 이상 관람가"),
    ADULT("청소년 관람불가");

    private final String description;

    Rating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
