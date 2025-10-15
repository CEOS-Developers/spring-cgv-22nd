package com.ceos22.cgv.module.snack.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.snack.dto.OrderRequest;
import com.ceos22.cgv.module.snack.dto.OrderResponse;
import com.ceos22.cgv.module.snack.service.OrderService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<OrderResponse>> order(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<OrderResponse> response = ApiResponse.of(
                orderService.order(request, user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<OrderResponse> response = ApiResponse.of(
                orderService.getOrder(orderId, user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/order/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails user) {

        ApiResponse<List<OrderResponse>> response = ApiResponse.of(
                orderService.myOrders(user.getUser()),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }


}
