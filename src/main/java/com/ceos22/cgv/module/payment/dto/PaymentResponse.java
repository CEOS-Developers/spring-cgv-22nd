package com.ceos22.cgv.module.payment.dto;

import com.ceos22.cgv.common.util.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        String paymentId,
        LocalDateTime paidAt,

        PaymentStatus paymentStatus,
        String orderName,
        String pgProvider,
        String currency,
        String customData
){ }