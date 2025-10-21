package com.ceos22.cgv.module.snack.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.snack.dto.OrderRequest;
import com.ceos22.cgv.module.snack.dto.OrderResponse;
import com.ceos22.cgv.module.snack.service.OrderService;
import com.ceos22.cgv.module.user.dto.CustomUserDetails;
import com.ceos22.cgv.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "스낵/음료 주문", description = "로그인한 사용자가 스낵/음료를 주문합니다. (인증 필요)")
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

    @Operation(summary = "주문 상세 조회", description = "주문 ID로 본인 소유의 주문 상세를 조회합니다. (인증 필요)")
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


    @Operation(summary = "나의 주문 목록", description = "로그인한 사용자의 주문 내역을 조회합니다. (인증 필요)")
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
