package com.ceos22.cgv.module.payment.dto;

import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.snack.domain.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentApiRequest(
        String storeId,
        String orderName,
        Integer totalPayAmount,
        String currency,
        String customData,
        Reservation reservation,
        Order order
) {
    public record PaymentApiRequestParameter(
            String storeId,
            String orderName,
            Integer totalPayAmount,
            String currency,
            String customData
    ){}

    public PaymentApiRequestParameter toParameter() {
        return new PaymentApiRequestParameter(
                this.storeId(),
                this.orderName(),
                this.totalPayAmount(),
                this.currency(),
                this.customData()
        );
    }
}