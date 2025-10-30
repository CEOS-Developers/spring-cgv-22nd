package com.ceos22.cgv_clone.domain.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private final PaymentConfig paymentConfig;

    public WebClientConfig(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    @Bean
    public WebClient paymentWebClient() {
        return WebClient.builder()
                .baseUrl(paymentConfig.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + paymentConfig.getApiSecret())
                .build();
    }
}
