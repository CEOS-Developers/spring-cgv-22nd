package com.ceos22.springcgv.external.payment;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentFeignConfig {

    @Value("${payment.api-secret}")
    private String paymentApiSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + paymentApiSecret);
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}