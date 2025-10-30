package com.ceos22.cgv_clone.domain.payment.dto.response;

import com.ceos22.cgv_clone.domain.payment.entity.Payment;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentId,
        String storeId,
        String orderName,
        Integer totalPayAmount,
        String currency,
        String customData,
        PaymentStatus status,
        String failReason,
        LocalDateTime paidAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentId(),
                payment.getStoreId(),
                payment.getOrderName(),
                payment.getTotalPayAmount(),
                payment.getCurrency(),
                payment.getCustomData(),
                payment.getStatus(),
                payment.getFailReason(),
                payment.getCreatedAt()
        );
    }
}