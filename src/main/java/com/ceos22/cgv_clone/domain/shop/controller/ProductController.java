package com.ceos22.cgv_clone.domain.shop.controller;

import com.ceos22.cgv_clone.domain.shop.dto.response.ProductResponseDto;
import com.ceos22.cgv_clone.domain.shop.service.ProductService;
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
    public List<ProductResponseDto> getAllProducts() {
        log.info("[GET] /products 요청 수신");
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public ProductResponseDto getProductById(@PathVariable Long productId) {
        log.info("[GET] /products/{} 요청 수신", productId);
        return productService.getProductById(productId);
    }
}
