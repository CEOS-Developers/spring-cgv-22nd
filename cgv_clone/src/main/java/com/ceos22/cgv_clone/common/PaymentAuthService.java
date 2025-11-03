package com.ceos22.cgv_clone.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentAuthService {

    private final WebClient rawPaymentWebClient; // Base URL만 가진 WebClient

    @Value("${payment.github-id}")
    private String githubId;

    private final AtomicReference<String> cachedSecret = new AtomicReference<>();

    @PostConstruct
    public void init() {
        refreshSecret();
    }

    public void refreshSecret() {
        // GET /auth/{githubId} -> String(시크릿)
        String secret = rawPaymentWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/auth/{githubId}").build(githubId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("결제 API Secret 발급 실패");
        }
        cachedSecret.set(secret.trim());
        log.info("Payment API Secret loaded.");
    }

    public String authorizationValue() {
        String secret = cachedSecret.get();
        if (secret == null) {
            refreshSecret();
            secret = cachedSecret.get();
        }
        return "Bearer " + secret;
    }

    /** 인증 헤더를 자동 주입한 WebClient 빌더 제공 */
    public WebClient withAuth(WebClient.Builder builder) {
        return builder
                .defaultHeader(HttpHeaders.AUTHORIZATION, authorizationValue())
                .build();
    }
}