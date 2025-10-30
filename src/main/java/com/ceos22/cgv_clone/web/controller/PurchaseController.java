package com.ceos22.cgv_clone.web.controller;

import com.ceos22.cgv_clone.global.apiPayload.ApiResponse;
import com.ceos22.cgv_clone.global.security.CustomUserDetails;
import com.ceos22.cgv_clone.web.dto.PurchaseRequestDto;
import com.ceos22.cgv_clone.web.dto.PurchaseResponseDto;
import com.ceos22.cgv_clone.web.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/products")
    @Operation(summary = "상품 구매 API",description = "영화관의 매점에서 상품을 구매하는 API입니다.")
    public ApiResponse<PurchaseResponseDto> purchase(@RequestBody PurchaseRequestDto purchaseRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(purchaseService.purchase(purchaseRequestDto,userDetails.user()));
    }
}
