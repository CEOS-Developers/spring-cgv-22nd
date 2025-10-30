package com.ceos22.spring_boot.domain.theater.api;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ceos22.spring_boot.domain.theater.dto.request.InstantPaymentRequest;
import com.ceos22.spring_boot.domain.theater.dto.response.InstantPaymentResponse;
import com.ceos22.spring_boot.domain.theater.exception.TheaterErrorCode;
import com.ceos22.spring_boot.global.exception.GlobalException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CeosPaymentClient {

	private final WebClient paymentWebClient;

	public InstantPaymentResponse payInstant(String paymentId, InstantPaymentRequest body){
		try{
			return paymentWebClient.post()
				.uri(uriBuilder -> uriBuilder.path("/payments/{paymentId}/instant").build(paymentId))
				.bodyValue(body)
				.retrieve()
				.onStatus(HttpStatusCode::isError, res ->
					res.bodyToMono(String.class)
						.flatMap(msg -> Mono.error(
							new GlobalException(TheaterErrorCode.PAYMENT_FAILED))))
				.bodyToMono(InstantPaymentResponse.class)
				.block();
		} catch (Exception e){
			throw new GlobalException(TheaterErrorCode.PAYMENT_SERVER_FAILED);
		}
	}

	public void cancel(String paymentId) {
		try {
			paymentWebClient.post()
				.uri(uriBuilder -> uriBuilder.path("/payments/{paymentId}/cancel").build(paymentId))
				.retrieve()
				.onStatus(HttpStatusCode::isError, res ->
					res.bodyToMono(String.class)
						.flatMap(msg -> Mono.error(
							new GlobalException(TheaterErrorCode.PAYMENT_FAILED))))
				.toBodilessEntity()
				.block();
		} catch (Exception e) {
			log.error("CEOS 결제 취소 실패", e);
			throw new GlobalException(TheaterErrorCode.PAYMENT_SERVER_FAILED);
		}
	}
}
