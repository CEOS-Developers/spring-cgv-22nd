package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.shop.entity.Product;
import com.ceos22.cgv_clone.domain.shop.dto.response.ProductResponseDto;
import com.ceos22.cgv_clone.domain.shop.repository.ProductRepository;
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
        log.debug("[SVC] getAllProducts start");
        try {
            List<ProductResponseDto> list = productRepository.findAll()
                    .stream()
                    .map(ProductResponseDto::fromEntity)
                    .toList();

            if (list.isEmpty()) {
                log.warn("[SVC] 상품 목록이 비어있습니다.");
            } else {
                log.info("[SVC] 상품 목록 조회 완료 - {}개", list.size());
            }
            return list;
        }
        catch (Exception e) {
            log.error("[SVC] 상품 목록 조회 실패", e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long productId) {
        log.debug("[SVC] getProductById start - productId={}", productId);
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

            log.info("[SVC] 상품 조회 완료 - id={}, name={}", product.getId(), product.getName());
            return ProductResponseDto.fromEntity(product);

        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(상품 조회) - productId={}, msg={}", productId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 상품 조회 실패 - productId={}", productId, e);
            throw e;
        }
    }
}
