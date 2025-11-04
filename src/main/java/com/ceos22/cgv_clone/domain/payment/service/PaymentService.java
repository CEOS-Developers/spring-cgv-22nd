package com.ceos22.cgv_clone.domain.payment.service;

import com.ceos22.cgv_clone.domain.payment.dto.PaymentRequestDto;
import com.ceos22.cgv_clone.domain.payment.dto.PaymentResponseDto;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient webClient;

    // Mock 결제 API 호출
    public PaymentResponseDto approvePayment(String paymentId, PaymentRequestDto requestDto) {

        log.info("Attempting payment for paymentId: {}", paymentId);

        return webClient.post()
                .uri("/payments/{paymentId}/instant", paymentId)
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("Payment API 호출 실패. status: {}, body: {}", response.statusCode(), errorBody);

                            return Mono.error(new CustomException(ErrorCode.PAYMENT_FAILED));
                        })
                )
                .bodyToMono(PaymentResponseDto.class)
                .block();
    }

    // 결제 취소 메서드 (결제 롤백)
    public void canclePayment(String paymentId) {
        log.warn("DB 저장 실패 시의 결제 취소 시도: {}", paymentId);

        webClient.post()
                .uri("/payments/{paymentId}/cancel", paymentId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> //
                        response.bodyToMono(String.class).flatMap(errorBody -> {
                            log.error("결제 취소까지 실패한 경우 paymentId: {}, body: {}",
                                    paymentId, errorBody);
                            return Mono.error(new CustomException(ErrorCode.PAYMENT_FAILED));
                        })
                )
                .bodyToMono(Void.class)
                .block();

        log.info("결제 취소 성공 paymentId: {}", paymentId);


    }
}
