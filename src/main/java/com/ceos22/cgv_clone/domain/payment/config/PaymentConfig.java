package com.ceos22.cgv_clone.domain.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment.mock")
@Getter
@Setter
public class PaymentConfig {

    private String baseUrl;
    private String apiSecret;
}
