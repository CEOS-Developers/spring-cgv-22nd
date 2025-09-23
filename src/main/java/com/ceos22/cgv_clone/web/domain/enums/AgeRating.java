package com.ceos22.cgv_clone.web.domain.enums;

public enum AgeRating {
    ALL(0),
    OVER_12(12),
    OVER_15(15),
    OVER_18(18);

    private final int ageLimit;

    AgeRating(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getAgeLimit() {
        return ageLimit;
    }
}
