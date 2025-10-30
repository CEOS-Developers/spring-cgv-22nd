package com.ceos22.cgv_clone.domain.store.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.store.dto.request.ProductCreateRequest;
import com.ceos22.cgv_clone.domain.store.dto.response.ProductResponse;
import com.ceos22.cgv_clone.domain.store.dto.response.ProductSimpleResponse;
import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import com.ceos22.cgv_clone.domain.store.entity.StoreType;
import com.ceos22.cgv_clone.domain.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductSimpleResponse> findProductsByStoreTypeAndCategory(StoreType storeType, ProductCategory category) {

        List<Product> products = productRepository.findByStoreTypeAndCategory(storeType, category);

        return products.stream()
                .map(ProductSimpleResponse::from)
                .collect(Collectors.toList());
    }

    public ProductResponse findProductDetail(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {

        Product product;

        if (Boolean.TRUE.equals(request.isCombo()) && request.comboItemIds() != null) {
            // 콤보 상품 생성
            product = Product.createCombo(
                    request.name(),
                    request.price(),
                    request.category(),
                    request.imageUrl()
            );

            List<Product> comboItems = productRepository.findAllById(request.comboItemIds());
            if (comboItems.size() != request.comboItemIds().size()) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
            }

            for (Product item : comboItems) {
                product.addComboItem(item);
            }
        } else {
            product = Product.of(
                    request.name(),
                    request.price(),
                    request.category(),
                    request.imageUrl()
            );
        }

        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }
}
