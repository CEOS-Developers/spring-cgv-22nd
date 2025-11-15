package com.ceos22.cgv_clone.domain.payment.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.payment.dto.request.PaymentCreateRequest;
import com.ceos22.cgv_clone.domain.payment.dto.response.PaymentResponse;
import com.ceos22.cgv_clone.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentCreateRequest request,
            @AuthenticationPrincipal Member member
    ) {
        PaymentResponse payment = paymentService.executePayment(member, request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, payment);
    }

    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable String paymentId,
            @AuthenticationPrincipal Member member
    ) {
        PaymentResponse payment = paymentService.cancelPayment(member, paymentId);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS, payment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable String paymentId,
            @AuthenticationPrincipal Member member
    ) {
        PaymentResponse payment = paymentService.getPayment(member, paymentId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, payment);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Member member
    ) {
        PaymentResponse payment = paymentService.getPaymentByOrder(member, orderId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, payment);
    }
}