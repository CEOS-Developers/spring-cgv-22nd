package com.ceos22.cgv_clone.domain.reservation;

public enum TicketPrice {
    GENERAL(12000),
    YOUTH(10000);

    private final int price;

    TicketPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
