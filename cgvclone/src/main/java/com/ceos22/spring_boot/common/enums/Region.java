package com.ceos22.spring_boot.common.enums;

public enum Region {
    SEOUL("서울"),
    GYEONGGI("경기"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천");

    private final String description;

    Region(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
