package com.ceos22.cgv_clone.domain.payment.service;

import com.ceos22.cgv_clone.domain.payment.dto.request.PaymentRequest;
import com.ceos22.cgv_clone.domain.payment.dto.response.PaymentCancelResponse;
import com.ceos22.cgv_clone.domain.payment.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestClient.Builder restClientBuilder;

    @Value("${payment.base-url}") private String baseUrl;
    @Value("${payment.secret}")   private String secret;
    @Value("${payment.store-id}") private String storeId;

    private RestClient client() {
        return restClientBuilder.baseUrl(baseUrl).build();
    }

    private String bearer() {
        return "Bearer " + secret;
    }

    public PaymentResponse pay(String paymentId, String orderName, int amount, String customJson) {
        PaymentRequest req = PaymentRequest.builder()
                .storeId(storeId)
                .orderName(orderName)
                .totalPayAmount(amount)
                .currency("KRW")
                .customData(customJson).build();

        return client().post()
                .uri("/payments/{paymentId}/instant", paymentId)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .body(req).retrieve()
                .body(PaymentResponse.class);
    }

    public PaymentCancelResponse cancel(String paymentId) {
        return client().post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .header(HttpHeaders.AUTHORIZATION, bearer())
                .retrieve()
                .body(PaymentCancelResponse.class);
    }
}
