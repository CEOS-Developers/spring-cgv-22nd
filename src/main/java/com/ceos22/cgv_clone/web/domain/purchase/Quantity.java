package com.ceos22.cgv_clone.web.domain.purchase;

public record Quantity(int value) {

    public static Quantity of(int value) {
        return new Quantity(value);
    }

    public void validatePositive() {
        if (value < 0) {
            throw new IllegalArgumentException("상품 개수는 0 이상이어야 합니다.");
        }
    }

    public Quantity add(Quantity other){
        return new Quantity(this.value + other.value);
    }

    public int getValue(){
        return value;
    }

}
