package com.ceos22.cgv_clone.api.controller;

import com.ceos22.cgv_clone.api.dto.CancelPaymentResponse;
import com.ceos22.cgv_clone.api.dto.InstantPaymentRequest;
import com.ceos22.cgv_clone.api.dto.PaymentResponse;
import com.ceos22.cgv_clone.service.PaymentClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentClient paymentClient;

    @PostMapping("/{paymentId}/instant")
    public PaymentResponse payInstant(
            @PathVariable String paymentId,
            @RequestBody InstantPaymentRequest request) {
        return paymentClient.pay(paymentId, request);
    }

    @PostMapping("/{paymentId}/cancel")
    public CancelPaymentResponse cancel(@PathVariable String paymentId) {
        return paymentClient.cancel(paymentId);
    }

    @GetMapping("/{paymentId}")
    public Object getPayment(@PathVariable String paymentId) {
        return paymentClient.getOne(paymentId);
    }
}