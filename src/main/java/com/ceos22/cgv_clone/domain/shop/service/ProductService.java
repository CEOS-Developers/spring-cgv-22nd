package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.shop.entity.Product;
import com.ceos22.cgv_clone.domain.shop.dto.response.ProductResponseDto;
import com.ceos22.cgv_clone.domain.shop.repository.ProductRepository;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        List<ProductResponseDto> list = productRepository.findAll()
                .stream()
                .map(ProductResponseDto::fromEntity)
                .toList();

        return list;

    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "ProductId=%d를 찾을 수 없습니다.".formatted(productId)));

        return ProductResponseDto.fromEntity(product);

    }
}
