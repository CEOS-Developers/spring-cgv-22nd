package com.ceos22.springcgv.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCancelResponseDto {
    private String paymentId;
    private String paymentStatus;
    private String orderName;
    private String pgProvider;
    private String currency;
    private String customData;
    private String paidAt;
}