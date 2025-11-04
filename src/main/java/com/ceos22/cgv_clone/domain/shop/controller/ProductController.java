package com.ceos22.cgv_clone.domain.shop.controller;

import com.ceos22.cgv_clone.domain.shop.dto.response.ProductResponseDto;
import com.ceos22.cgv_clone.domain.shop.service.ProductService;
import com.ceos22.cgv_clone.global.code.SuccessCode;
import com.ceos22.cgv_clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ApiResponse<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ApiResponse.of(products, SuccessCode.GET_SUCCESS);
    }

    @GetMapping("/products/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long productId) {
        ProductResponseDto product = productService.getProductById(productId);
        return ApiResponse.of(product, SuccessCode.GET_SUCCESS);
    }
}
