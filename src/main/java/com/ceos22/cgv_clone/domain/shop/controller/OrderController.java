package com.ceos22.cgv_clone.domain.shop.controller;

import com.ceos22.cgv_clone.domain.shop.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.SimpleResponse;
import com.ceos22.cgv_clone.domain.shop.service.OrderService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public OrderResponseDto createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderRequestDto orderRequestDto) {
        int itemCount = orderRequestDto.getItems() == null ? 0 : orderRequestDto.getItems().size();
        log.info("[POST] /orders 요청 수신 - user={}, theaterId={}, items={}",
                userDetails.getUsername(), orderRequestDto.getTheaterId(), itemCount);
        return orderService.createOrder(userDetails.getUsername(), orderRequestDto);
    }

    @GetMapping("/orders/{orderId}")
    public OrderResponseDto getOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        log.info("[GET] /orders/{} 요청 수신 - user={}", orderId, userDetails.getUsername());
        return orderService.getOrder(userDetails.getUsername(), orderId);
    }

    @DeleteMapping("/orders/cancel/{orderId}")
    public ApiResponse<SimpleResponse> cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        orderService.cancelOrder(userDetails.getUsername(), orderId);
        return ApiResponse.of(new SimpleResponse("주문이 취소되었습니다."), SuccessCode.DELETE_SUCCESS);
    }

    @PostMapping("/orders/{orderId}/pay")
    public ApiResponse<SimpleResponse> payOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        orderService.payForOrder(userDetails.getUsername(), orderId);
        return ApiResponse.of(new SimpleResponse("주문이 완료되었습니다."), SuccessCode.CREATE_SUCCESS);
    }
}
