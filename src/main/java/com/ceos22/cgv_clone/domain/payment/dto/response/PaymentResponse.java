package com.ceos22.cgv_clone.domain.payment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String paymentId;
    private String paidAt; // 성공 시 존재
}
