package com.ceos22.cgv_clone.web.dto;

import lombok.Getter;

public class PaymentResponseDto {

    @Getter
    public static class PaymentDto {
        private String paymentId;
        private String paidAt;
    }

    @Getter
    public static class PaymentDetailResponseDto {
        String paymentId;
        String paymentStatus;
        String orderName;
        String pgProvider;
        String currency;
        String customData;
        String paidAt;
    }
}
