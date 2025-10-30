package com.ceos22.cgv_clone.api.dto;

public record InstantPaymentRequest(
        String storeId,
        String orderName,
        int totalPayAmount,
        String currency,
        String customData
) {}
