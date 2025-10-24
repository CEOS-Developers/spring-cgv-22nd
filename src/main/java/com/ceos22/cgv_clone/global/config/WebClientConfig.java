package com.ceos22.cgv_clone.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${payment.api-secret}") String apiSecret;

    @Value("${payment.timeout-ms}") long timeoutMs;

    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+apiSecret)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create()
                                        .responseTimeout(Duration.ofMillis(3000))
                        )
                )
                .filter((request, next) ->
                        next.exchange(request)
                                .flatMap(response -> {
                                    if (response.statusCode().isError()) {
                                        return response.createException()
                                                .flatMap(Mono::error);
                                    }
                                    return Mono.just(response);
                                })
                                .retryWhen(
                                        Retry.backoff(2, Duration.ofMillis(200))
                                                .filter(throwable -> {
                                                    // retry 조건: 5xx 또는 네트워크 예외 등
                                                    if (throwable instanceof WebClientResponseException) {
                                                        return ((WebClientResponseException) throwable)
                                                                .getStatusCode().is5xxServerError();
                                                    }
                                                    if (throwable instanceof WebClientRequestException) {
                                                        return true; // 네트워크 오류 재시도
                                                    }
                                                    return false;
                                                })
                                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                                        new IllegalArgumentException("결제 API 재시도 실패: attempts=" + retrySignal.totalRetries(), retrySignal.failure())
                                                )
                                )
                )
                .build();
    }
}
