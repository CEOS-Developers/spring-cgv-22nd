package com.ceos22.springcgv.controller.purchase;

import com.ceos22.springcgv.config.CustomUserDetails;
import com.ceos22.springcgv.dto.purchase.PurchaseRequestDto;
import com.ceos22.springcgv.global.response.ApiResponse;
import com.ceos22.springcgv.service.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    /**
     * 매점 상품 구매
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPurchase(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PurchaseRequestDto requestDto) {

        Long currentUserId = userDetails.getUserId();
        Long purchaseId = purchaseService.createPurchase(currentUserId, requestDto);

        ApiResponse<Long> response = ApiResponse.success(201, "상품 구매에 성공했습니다.", purchaseId);
        return ResponseEntity.ok(response);
    }

    /**
     * 매점 상품 구매 취소
     * @param 상품 구매 ID
     * @return
     */
    @PostMapping("/{purchaseId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPurchase(@PathVariable Long purchaseId) {
        purchaseService.cancelPurchase(purchaseId);

        ApiResponse<Void> response = ApiResponse.success(200, "결제가 취소되었습니다.");
        return ResponseEntity.ok(response);
    }
}