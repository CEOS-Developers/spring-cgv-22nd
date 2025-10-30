package com.ceos22.cgv_clone.domain.payment.client;

import com.ceos22.cgv_clone.domain.payment.dto.request.InstantPaymentRequest;
import com.ceos22.cgv_clone.domain.payment.dto.response.InstantPaymentResponse;
import com.ceos22.cgv_clone.domain.payment.dto.response.PaymentDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClient {

    private final WebClient paymentWebClient;

    public InstantPaymentResponse instantPayment(String paymentId, InstantPaymentRequest request) {
        log.info("즉시 결제 요청 - paymentId: {}, storeId: {}", paymentId, request.storeId());

        return paymentWebClient.post()
                .uri("/payments/{paymentId}/instant", paymentId)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("결제 요청 실패 (4xx) - paymentId: {}, body: {}", paymentId, body);
                                    return Mono.error(new RuntimeException("결제 요청 실패: " + body));
                                })
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("결제 처리 실패 (5xx) - paymentId: {}, body: {}", paymentId, body);
                                    return Mono.error(new RuntimeException("결제 처리 실패: " + body));
                                })
                )
                .bodyToMono(InstantPaymentResponse.class)
                .block();
    }


    public PaymentDetailResponse cancelPayment(String paymentId) {
        log.info("결제 취소 요청 - paymentId: {}", paymentId);

        return paymentWebClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("결제 취소 실패 - paymentId: {}, body: {}", paymentId, body);
                                    return Mono.error(new RuntimeException("결제 취소 실패: " + body));
                                })
                )
                .bodyToMono(PaymentDetailResponse.class)
                .block();
    }
}
