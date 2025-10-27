package com.ceos22.cgv_clone.api.dto;

public record CancelPaymentResponse(
        String paymentId,
        String paymentStatus,
        String orderName,
        String pgProvider,
        String currency,
        String customData,
        String paidAt
) {}
