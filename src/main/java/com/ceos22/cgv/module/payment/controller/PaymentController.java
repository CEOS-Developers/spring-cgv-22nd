package com.ceos22.cgv.module.payment.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.common.response.ApiResponse;
import com.ceos22.cgv.module.payment.dto.PaymentRequest;
import com.ceos22.cgv.module.payment.dto.PaymentResponse;
import com.ceos22.cgv.module.payment.service.PaymentService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제", description = "결제를 위한 엔드포인트입니다. (인증 필요)")
    @PostMapping("/payments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> payments(
            @Valid @RequestBody PaymentRequest paymentRequest,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<PaymentResponse> response = ApiResponse.of(
                paymentService.createPayment(
                        paymentRequest,
                        user.getUser()
                ),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "결제 내역 조회", description = "결제 내역 단건 조회 위한 엔드포인트입니다. (인증 필요)")
    @GetMapping("/payments/{paymentLogId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayments(
            @PathVariable Long paymentLogId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<PaymentResponse> response = ApiResponse.of(
                paymentService.getPayment(paymentLogId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "결제 취소", description = "결제 취소를 위한 엔드포인트입니다. (인증 필요)")
    @GetMapping("/payments/{paymentLogId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayments(
            @PathVariable Long paymentLogId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<PaymentResponse> response = ApiResponse.of(
                paymentService.cancelPayment(paymentLogId),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }
}
