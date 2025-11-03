package com.ceos22.cgv_clone.api.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.netty.http.client.HttpClient;

import static java.time.Duration.ofSeconds;

@Configuration
public class WebClientConfig implements WebMvcConfigurer {

    @Bean
    public ReactorClientHttpConnector paymentHttpConnector(
            @Value("${payment.timeout-seconds:3}") int timeoutSeconds) {
        var httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeoutSeconds * 1000)
                .responseTimeout(ofSeconds(timeoutSeconds))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(timeoutSeconds))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds)));
        return new ReactorClientHttpConnector(httpClient);
    }

    @Bean
    public ExchangeStrategies webClientExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(512 * 1024))
                .build();
    }

    /** 인증 전용 호출용 RAW 클라이언트 */
    @Bean
    public WebClient rawPaymentWebClient(
            @Value("${payment.base-url}") String baseUrl,
            ReactorClientHttpConnector connector,
            ExchangeStrategies strategies) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .clientConnector(connector)
                .build();
    }
}
