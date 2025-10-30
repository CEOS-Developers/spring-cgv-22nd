package com.ceos22.cgv_clone.domain.theater.entity;

public enum ScreenType {
    SCREENX("SCREENX"),
    FOUR_DX("4DX"),
    IMAX("IMAX"),
    DOLBY_ATMOS("DOLBY ATMOS"),
    DRIVE_IN("자동차상영관"),
    GOLD_CLASS("골드클래스"),
    CINE_AND_LIVING_ROOM("씨네앤리빙룸"),
    TEMPUR_CINEMA("TEMPUR CINEMA"),
    CDC("CDC"),
    CINE_AND_FORET("씨네앤포레"),
    PREMIUM("프리미엄관"),
    PRIVATE_CINEMA("프라이빗 시네마"),
    THE_ART_HOUSE("아트하우스"),
    CINE_KIDS("CINE KIDS"),
    SUITE_CINEMA("SUITE CINEMA"),
    PRIVATE_BOX("PRIVATE BOX");

    private final String screenType;

    ScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String screenType() {
        return screenType;
    }
}
