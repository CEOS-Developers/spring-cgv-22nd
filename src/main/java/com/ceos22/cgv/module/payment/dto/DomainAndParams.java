package com.ceos22.cgv.module.payment.dto;

import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.snack.domain.Order;

public record DomainAndParams(
        String orderName,
        Integer totalPayAmount,
        String currency,
        String customData,
        Reservation reservation,
        Order order
) {

}