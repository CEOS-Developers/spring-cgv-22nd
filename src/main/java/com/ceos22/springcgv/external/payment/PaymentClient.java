package com.ceos22.springcgv.external.payment;

import com.ceos22.springcgv.dto.payment.PaymentCancelResponseDto;
import com.ceos22.springcgv.dto.payment.PaymentRequestDto;
import com.ceos22.springcgv.dto.payment.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "paymentClient",
        url = "${payment.base-url}",
        configuration = PaymentFeignConfig.class
)
public interface PaymentClient {

    @PostMapping(value = "/payments/{paymentId}/instant", consumes = MediaType.APPLICATION_JSON_VALUE)
    PaymentResponseDto requestPayment(
            @PathVariable("paymentId") String paymentId,
            @RequestBody PaymentRequestDto request
    );

    @PostMapping(value = "/payments/{paymentId}/cancel", consumes = MediaType.APPLICATION_JSON_VALUE)
    PaymentCancelResponseDto cancelPayment(
            @PathVariable("paymentId") String paymentId
    );
}