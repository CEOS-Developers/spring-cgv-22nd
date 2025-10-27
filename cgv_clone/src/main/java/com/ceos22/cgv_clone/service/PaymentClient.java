package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.api.dto.CancelPaymentResponse;
import com.ceos22.cgv_clone.api.dto.PaymentRequest;
import com.ceos22.cgv_clone.api.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class PaymentClient {

    private final WebClient paymentWebClient;

    public PaymentResponse pay(String paymentId, PaymentRequest request) {
        return paymentWebClient.post()
                .uri("/payments/{paymentId}/pay", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block(); // 동기 방식
    }

    public CancelPaymentResponse cancel(String paymentId) {
        return paymentWebClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .retrieve()
                .bodyToMono(CancelPaymentResponse.class)
                .block();
    }

    public Object getOne(String paymentId) {
        return paymentWebClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

}