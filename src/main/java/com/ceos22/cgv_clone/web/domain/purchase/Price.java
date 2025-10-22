package com.ceos22.cgv_clone.web.domain.purchase;

public record Price(int value) {

    public static Price of(int value) {
        return new Price(value);
    }

    public void validateNonNegative() {
        if (value < 0) {
            throw new IllegalArgumentException("금액은 음수일 수 없습니다.");
        }
    }

    public Price multiply(Quantity quantity) {
        return new Price(this.value * quantity.getValue());
    }

    public Price add(Price other){
        return new Price(this.value + other.value);
    }

    public int getValue() {
        return value;
    }
}
