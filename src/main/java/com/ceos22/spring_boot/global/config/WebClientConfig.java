package com.ceos22.spring_boot.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

	@Bean
	public WebClient kakaoAuthWebClient(){
		return WebClient.builder()
			.baseUrl("https://kauth.kakao.com")
			.defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
			.build();
	}

	@Bean
	public WebClient kakaoApiWebClient(){
		return WebClient.builder()
			.baseUrl("https://kapi.kakao.com")
			.defaultHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
			.build();
	}

	@Bean
	public WebClient paymentWebClient(
		@Value("${payment.ceos.base-url:https://payment.loopz.co.kr}") String baseUrl,
		@Value("${payment.ceos.api-secret}") String apiSecret
	){
		return WebClient.builder()
			.baseUrl(baseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiSecret)
			.build();
	}

}
