package com.ceos22.cgv.module.payment.service;

import com.ceos22.cgv.module.payment.dto.PaymentApiRequest;
import com.ceos22.cgv.module.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PaymentApiService {

    private final RestClient restClient;

    @Value("${payment.secret-key}")
    private String SECRET_KEY;
    @Value("${payment.store-id}")
    private String STORE_ID;
    private final String STORE_PREFIX = "CEOS-22-";

    @Value("${payment.base-url}")
    private String BASE_URL;
    private final String PG_PROVIDER = "CEOS_PAY";

    // 외부 결제 요청
    public PaymentResponse createPaymentAPI(String paymentId, PaymentApiRequest request) {

        try{
            ResponseEntity<PaymentResponse> response = restClient
                    .post()
                    .uri(BASE_URL + "/payments/{paymentId}/instant", paymentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + SECRET_KEY)
                    .body(request.toParameter())
                    .retrieve()
                    .toEntity(PaymentResponse.class);

            return response.getBody();

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), "결제 요청 실패: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "결제 요청 중 알 수 없는 오류", ex);
        }
    }

    // 외부 결제 내역 조회
    public PaymentResponse getPaymentAPI(String paymentId) {
        try {
            ResponseEntity<PaymentResponse> response = restClient
                    .get()
                    .uri(BASE_URL + "/payments/{paymentId}", paymentId)
                    .header("Authorization", "Bearer " + SECRET_KEY)
                    .retrieve()
                    .toEntity(PaymentResponse.class);

            return response.getBody();

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), "결제 내역 조회 실패: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "결제 내역 조회 중 알 수 없는 오류", ex);
        }
    }

    // 외부 결제 취소 요청
    public PaymentResponse cancelPaymentAPI(String paymentId) {
        try {
            ResponseEntity<PaymentResponse> response = restClient
                    .post()
                    .uri(BASE_URL + "/payments/{paymentId}/cancel", paymentId)
                    .header("Authorization", "Bearer " + SECRET_KEY)
                    .retrieve()
                    .toEntity(PaymentResponse.class);

            return response.getBody();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 취소 요청 실패");
        }
    }
}
