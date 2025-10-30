package com.ceos22.cgv_clone.domain.store.controller;

import com.ceos22.cgv_clone.domain.store.dto.ProductOrderDetailDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderRequestDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderResponseDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderSummaryDto;
import com.ceos22.cgv_clone.domain.store.service.ProductOrderQueryService;
import com.ceos22.cgv_clone.global.apiPayload.CustomResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="매점 관련 API", description = "매점 주문 / 목록 조회 / 단건 조회")
public class ProductOrderQueryController {

    private final ProductOrderQueryService productOrderService;

    @Operation(summary = "주문 생성")
    @PostMapping("/stores/orders")
    public CustomResponse<ProductOrderResponseDto> create(
            @RequestBody ProductOrderRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        return CustomResponse.onSuccess(SuccessCode.CREATED, productOrderService.createOrder(email, request));
    }

    @Operation(summary = "주문 목록 조회", description = "회원의 주문 목록을 조회하는 API")
    @GetMapping("/stores/orders")
    public CustomResponse<List<ProductOrderSummaryDto>> list(@RequestParam Long memberId) {
        return CustomResponse.onSuccess(SuccessCode.OK, productOrderService.getOrdersByMember(memberId));
    }

    @Operation(summary = "주문 상세 조회")
    @GetMapping("/stores/orders/{orderId}")
    public CustomResponse<ProductOrderDetailDto> detail(@PathVariable Long orderId) {
        return CustomResponse.onSuccess(SuccessCode.OK, productOrderService.getOrder(orderId));
    }
}
