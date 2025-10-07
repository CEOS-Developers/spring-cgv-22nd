package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.dto.PurchaseRequestDto;
import com.ceos22.cgv_clone.web.dto.PurchaseResponseDto;
import com.ceos22.cgv_clone.web.repository.CinemaRepository;
import com.ceos22.cgv_clone.web.repository.ProductRepository;
import com.ceos22.cgv_clone.web.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CinemaRepository cinemaRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PurchaseResponseDto purchase(PurchaseRequestDto purchaseRequestDto, User user) {
        Cinema cinema = cinemaRepository.findById(purchaseRequestDto.getCinemaId())
                .orElseThrow(()-> new GeneralException(ErrorStatus.CINEMA_NOT_FOUND));

        List<Long> productIds = purchaseRequestDto.getProducts().stream()
                .map(PurchaseRequestDto.ProductDto::getProductId)
                .collect(Collectors.toList());
        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND);
        }

        Purchase purchase = Purchase.builder()
                .user(user)
                .cinema(cinema)
                .totalPrice(0)
                .totalQuantity(0)
                .build();

        int sumPrice = 0;
        int sumQuantity = 0;
        for (PurchaseRequestDto.ProductDto productDto : purchaseRequestDto.getProducts()) {
            Product product = products.stream()
                    .filter(p->p.getId().equals(productDto.getProductId()))
                    .findFirst()
                    .orElseThrow(()-> new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND));

            int quantity = productDto.getQuantity();
            if (quantity <= 0){
                throw new GeneralException(ErrorStatus.INVALID_QUANTITY);
            }

            int unitPrice = product.getPrice() * quantity;

            PurchaseProduct purchaseProduct = PurchaseProduct.builder()
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .purchase(purchase)
                    .build();

            sumPrice += unitPrice;
            sumQuantity += quantity;
        }

        purchase.setTotal(sumQuantity, sumPrice);
        purchaseRepository.save(purchase);

        List<PurchaseResponseDto.ProductDto> productDtos = purchase.getProducts().stream()
                .map(product -> PurchaseResponseDto.ProductDto.of(
                        product.getProduct(),
                        product.getQuantity(),
                        product.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return PurchaseResponseDto.builder()
                .products(productDtos)
                .purchaseId(purchase.getId())
                .totalPrice(purchase.getTotalPrice())
                .totalQuantity(purchase.getTotalQuantity())
                .build();

    }
}
