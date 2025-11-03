package com.ceos22.cgv_clone.api.config;

import com.ceos22.cgv_clone.common.PaymentAuthService;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig implements WebMvcConfigurer {

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${payment.timeout-seconds:3}")
    private int timeoutSeconds;

    private final PaymentAuthService paymentAuthService;

    private HttpClient httpClient(int timeoutSeconds) {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
                .responseTimeout(Duration.ofSeconds(timeoutSeconds))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(timeoutSeconds))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds)));
    }

    private ExchangeStrategies exchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(cfg -> cfg.defaultCodecs().maxInMemorySize(512 * 1024))
                .build();
    }

    /** 인증 전용 (auth/{githubId}) */
    @Bean
    public WebClient rawPaymentWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(exchangeStrategies())
                .clientConnector(new ReactorClientHttpConnector(httpClient(timeoutSeconds)))
                .build();
    }

    /** 인증 자동 부착 */
    @Bean
    public WebClient paymentWebClient() {
        return paymentAuthService.withAuth(
                WebClient.builder()
                        .baseUrl(baseUrl)
                        .exchangeStrategies(exchangeStrategies())
                        .clientConnector(new ReactorClientHttpConnector(httpClient(timeoutSeconds)))
        );
    }
}
