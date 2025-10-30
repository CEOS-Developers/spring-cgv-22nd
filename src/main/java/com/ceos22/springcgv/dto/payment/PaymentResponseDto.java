package com.ceos22.springcgv.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {
    private String paymentId;
    private String paidAt;
}
