package com.ceos22.cgv_clone.domain.payment.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequest {
    private String storeId;
    private String orderName;
    private int totalPayAmount;
    private String currency;   // "KRW"
    private String customData; // JSON string(optional)
}
