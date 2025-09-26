package com.ceos22.cgv_clone.web.domain.enums;

public enum AgeRating {
    ALL(0,"전체연령가"),
    OVER_12(12,"12세 이상"),
    OVER_15(15,"15세 이상"),
    OVER_18(18,"18세 이상");

    private final int ageLimit;
    private final String description;

    AgeRating(int ageLimit, String description) {
        this.ageLimit = ageLimit;
        this.description = description;
    }

    public int getAgeLimit() {
        return ageLimit;
    }
    public String getDescription() {return description;}
}
