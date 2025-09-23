package com.ceos22.cgv_clone.domain.product.entity;

public enum ProductCategory {
    COMBO("콤보"),
    POPCORN("팝콘"),
    DRINK("음료"),
    SNACK("스낵"),
    CHARACTER_GOODS("캐릭터굿즈"),
    SET("세트"),
    SNACK_PIZZA("스낵/피자"),
    PHOTOPLAY("포토플레이"),
    COFFEE("커피");

    private final String categoryName;

    ProductCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String CategoryName() {
        return categoryName;
    }
}
