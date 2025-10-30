package com.ceos22.cgv_clone.domain.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest (
        @NotNull(message = "주문 ID는 필수입니다")
        Long orderId
){
}
