package com.ceos22.spring_boot.domain.order;

import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.domain.order.dto.PurchaseRequestDto;
import com.ceos22.spring_boot.domain.order.dto.PurchaseResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.common.response.status.SuccessStatus;

@RestController
@RequestMapping("/store")
@Tag(name = "Order API", description = "상품 주문 관련 API")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @Operation(
            summary = "매점 구매 (결제 즉시 확정)",
            description = "인증된 사용자의 매점 구매를 처리합니다. 재고/가용성 검증 후 주문 생성, 결제 생성까지 진행됩니다."
    )
    @PostMapping(
            value = "/purchase",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<PurchaseResponseDto>> purchase(
            @Valid @RequestBody PurchaseRequestDto req,
            @AuthenticationPrincipal CustomUserPrincipal me
    ) {
        PurchaseResponseDto res = orderService.purchase(me.getUserId(), req);
        return ApiResponse.onSuccess(SuccessStatus._OK, res);
    }
}