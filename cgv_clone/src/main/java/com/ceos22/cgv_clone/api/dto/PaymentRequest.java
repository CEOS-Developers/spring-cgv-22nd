package com.ceos22.cgv_clone.api.dto;

public record PaymentRequest(
        String storeId,
        String orderName,
        int totalPayAmount,
        String currency,
        String customData
) {}
