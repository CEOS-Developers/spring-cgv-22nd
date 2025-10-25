package com.ceos22.cgv.module.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.AssertTrue;

public record PaymentRequest(
        @Nullable Long reservationId,
        @Nullable Long orderId) {

    @Schema(hidden = true)
    @AssertTrue(message = "reservationId 또는 orderId 중 하나만 전달하세요.")
    public boolean isOnlyOne() {
        return (reservationId != null) ^ (orderId != null);
    }

}