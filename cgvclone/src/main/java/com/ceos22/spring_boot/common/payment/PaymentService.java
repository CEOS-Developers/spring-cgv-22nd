package com.ceos22.spring_boot.common.payment;

import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient paymentWebClient;

    @Transactional
    public PaymentResponse requestPayment(String paymentId, PaymentRequest request) {
        log.info("[결제 요청] paymentId={}, amount={}, store={}", paymentId, request.totalPayAmount(), request.storeId());
        return paymentWebClient.post()
                .uri("/payments/{paymentId}/instant", paymentId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .doOnNext(body -> log.error("[결제 서버 오류 응답]: {}", body))
                                .then(Mono.error(new GeneralException(ErrorStatus._EXTERNAL_API_ERROR, "결제 요청 실패")))
                )
                .bodyToMono(PaymentResponse.class)
                .block();
    }


    @Transactional
    public void cancelPayment(String paymentId) {
        paymentWebClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new GeneralException(ErrorStatus._EXTERNAL_API_ERROR, "결제 취소 실패")))
                .toBodilessEntity()
                .block();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        return paymentWebClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        Mono.error(new GeneralException(ErrorStatus._EXTERNAL_API_ERROR, "결제 조회 실패")))
                .bodyToMono(PaymentResponse.class)
                .block();
    }


    public record PaymentRequest(
            String storeId,
            String orderName,
            int totalPayAmount,
            String currency,
            String customData
    ) {}

    public record PaymentResponse(
            String paymentId,
            String paidAt
    ) {}
}
