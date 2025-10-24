package com.ceos22.cgv_clone.global.service;

import com.ceos22.cgv_clone.web.dto.PaymentRequestDto;
import com.ceos22.cgv_clone.web.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentClient {
    private final WebClient paymentWebClient;

    public PaymentResponseDto.PaymentDto payInstant(String paymentId, PaymentRequestDto req) {
        return paymentWebClient.post()
                .uri("/payments/{paymentId}/instant", paymentId)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponseDto.PaymentDto.class)
                .block();
    }

    public PaymentResponseDto.PaymentDetailResponseDto cancelPayment(String paymentId) {
        return paymentWebClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .retrieve()
                .bodyToMono(PaymentResponseDto.PaymentDetailResponseDto.class)
                .block();
    }

    public PaymentResponseDto.PaymentDetailResponseDto getPayment(String paymentId) {
        return paymentWebClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .retrieve()
                .bodyToMono(PaymentResponseDto.PaymentDetailResponseDto.class)
                .block();
    }
}
