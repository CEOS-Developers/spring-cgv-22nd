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
}
