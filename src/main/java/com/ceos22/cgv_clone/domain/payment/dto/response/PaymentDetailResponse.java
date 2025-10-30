package com.ceos22.cgv_clone.domain.payment.dto.response;

public record PaymentDetailResponse(
        String paymentId,
        String paymentStatus,
        String orderName,
        String pgProvider,
        String currency,
        String customData,
        String paidAt
) {
}