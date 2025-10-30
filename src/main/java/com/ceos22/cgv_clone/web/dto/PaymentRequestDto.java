package com.ceos22.cgv_clone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentRequestDto{

    String storeId;
    String orderName;
    Integer totalPayAmount;
    String currency;
    String customData;

    public static PaymentRequestDto of(String storeId, String orderName, Integer totalPayAmount, String currency, String customData) {
        return new PaymentRequestDto(
                storeId,
                orderName,
                totalPayAmount,
                currency,
                customData);
    }
}
