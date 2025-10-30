package com.ceos22.cgv_clone.domain.payment.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentQueryResponse {
    private String paymentId;
    private String paymentStatus;
    private String orderName;
    private String pgProvider;
    private String currency;
    private String customData;
    private String paidAt;
}
