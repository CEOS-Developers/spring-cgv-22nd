package com.ceos22.springcgv.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    private String storeId;
    private String orderName;
    private Integer totalPayAmount;
    private String currency; // KRW or USD
    private String customData; // optional JSON string
}
