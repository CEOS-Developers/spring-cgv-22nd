package com.ceos22.cgv_clone.domain.store.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.store.dto.response.StoreProductResponse;
import com.ceos22.cgv_clone.domain.store.dto.response.StoreResponse;
import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import com.ceos22.cgv_clone.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getStores(
            @RequestParam Long theaterId
    ) {
        List<StoreResponse> stores = storeService.findStoresByTheater(theaterId);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, stores);
    }

    @GetMapping("/{storeId}/products")
    public ResponseEntity<ApiResponse<List<StoreProductResponse>>> getStoreProducts(
            @PathVariable Long storeId,
            @RequestParam ProductCategory category
    ) {
        List<StoreProductResponse> products = storeService.findProductsByStoreAndCategory(storeId, category);
        return ApiResponse.success(SuccessCode.GET_SUCCESS, products);
    }
}
