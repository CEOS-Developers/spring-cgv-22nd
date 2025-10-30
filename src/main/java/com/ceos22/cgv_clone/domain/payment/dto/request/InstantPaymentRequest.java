package com.ceos22.cgv_clone.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InstantPaymentRequest (
        @NotBlank(message = "매점 ID는 필수입니다.")
        String storeId,

        @NotBlank(message = "주문명은 필수입니다.")
        String orderName,

        @NotNull(message = "결제 금액은 필수입니다.")
        @Positive(message = "결제 금액은 0이상이여야 합니다.")
        Integer totalPayAmount,

        @NotBlank(message = "통화는 필수입니다.")
        String currency,

        String customData
){
}
