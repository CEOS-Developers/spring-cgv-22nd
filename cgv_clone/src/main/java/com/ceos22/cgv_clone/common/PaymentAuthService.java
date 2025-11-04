package com.ceos22.cgv_clone.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicReference;


@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentAuthService {

    private final WebClient rawPaymentWebClient;

    @Value("${payment.github-id}")
    private String githubId;

    private final AtomicReference<String> cached = new java.util.concurrent.atomic.AtomicReference<>();

    @PostConstruct
    public void init() { refreshSecret(); }

    public void refreshSecret() {
        String secret = rawPaymentWebClient.get()
                .uri("/auth/{githubId}", githubId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("결제 API Secret 발급 실패");
        }
        cached.set(secret.trim());
    }

    public String authorizationValue() {
        String s = cached.get();
        if (s == null) { refreshSecret(); s = cached.get(); }
        return "Bearer " + s;
    }

    /** 인증 헤더를 자동 붙여주는 WebClient 생성 */
    public WebClient withAuth(
            WebClient.Builder builder) {
        return builder.defaultHeader(org.springframework.http.HttpHeaders.AUTHORIZATION, authorizationValue())
                .build();
    }
}