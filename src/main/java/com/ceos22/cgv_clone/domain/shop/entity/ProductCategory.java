package com.ceos22.cgv_clone.domain.shop.entity;

public enum ProductCategory {
    POPCORN("팝콘"),
    SNACK("스낵"),
    DRINK("음료");

    private final String description;

    ProductCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
