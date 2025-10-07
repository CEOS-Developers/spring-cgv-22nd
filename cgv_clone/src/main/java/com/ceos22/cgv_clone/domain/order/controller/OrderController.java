package com.ceos22.cgv_clone.domain.order.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.order.dto.request.OrderCreateRequest;
import com.ceos22.cgv_clone.domain.order.dto.response.OrderResponse;
import com.ceos22.cgv_clone.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal Member member
    ) {
        OrderResponse order = orderService.createOrder(member.getMemberId(), request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, order);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal Member member
    ) {
        List<OrderResponse> orders = orderService.getMyOrders(member.getMemberId());
        return ApiResponse.success(SuccessCode.GET_SUCCESS, orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Member member
    ) {
        OrderResponse order = orderService.getOrderDetail(member.getMemberId(), orderId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, order);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Member member
    ) {
        orderService.cancelOrder(member.getMemberId(), orderId);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }
}
