package com.ceos22.cgv_clone.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentResponseDto {

    private String paymentId;
    private LocalDateTime paidAt;
}
