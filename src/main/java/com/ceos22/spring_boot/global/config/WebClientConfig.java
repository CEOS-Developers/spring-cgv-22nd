package com.ceos22.spring_boot.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

}
