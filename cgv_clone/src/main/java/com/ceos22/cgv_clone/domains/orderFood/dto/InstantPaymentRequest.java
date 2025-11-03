package com.ceos22.cgv_clone.domains.orderFood.dto;

public record InstantPaymentRequest(
        String storeId,
        String orderName,
        int totalPayAmount,
        String currency,
        String customData
) {}
