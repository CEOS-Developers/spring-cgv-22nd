package com.ceos22.cgv_clone.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record InstantPaymentResponse(
        String paymentId,

        @JsonProperty("paidAt")
        LocalDateTime paidAt
) {
}
