package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.service.RedisService;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.dto.PurchaseRequestDto;
import com.ceos22.cgv_clone.web.dto.PurchaseResponseDto;
import com.ceos22.cgv_clone.web.repository.CinemaRepository;
import com.ceos22.cgv_clone.web.repository.ProductRepository;
import com.ceos22.cgv_clone.web.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final CinemaRepository cinemaRepository;
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;
    private final RedisService redisService;

    @Transactional
    public PurchaseResponseDto purchase(PurchaseRequestDto purchaseRequestDto, User user) {
        Cinema cinema = cinemaRepository.findById(purchaseRequestDto.getCinemaId())
                .orElseThrow(()-> new GeneralException(ErrorStatus.CINEMA_NOT_FOUND));

        List<Long> productIds = purchaseRequestDto.getProducts().stream()
                .map(PurchaseRequestDto.ProductDto::getProductId)
                .collect(Collectors.toList());

        List<RLock> locks = new ArrayList<>();
        try{
            for (Long productId : productIds) {
                RLock lock = redissonClient.getLock("productLock: " + productId);
                boolean locked = lock.tryLock(0,5, TimeUnit.SECONDS);
                if (!locked) {
                    throw new GeneralException(ErrorStatus.PRODUCT_LOCK_FAILED);
                }
                locks.add(lock);
            }

            //캐시에서 재고 조회 -> 미스발생 시 DB에서 로드 후 캐시에 저장
            List<Product> products = productRepository.findAllById(productIds);

            if (products.size() != productIds.size()) {
                throw new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND);
            }
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));

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

                //캐시에서 재고 조회
                String cacheKey = "product:stock:" + product.getId();
                Integer cacheStock = (Integer) redisService.getValue(cacheKey);

                if (!cacheStock.equals(product.getStock())){
                    cacheStock = product.getStock();
                    redisService.setValue(cacheKey,cacheStock);
                }

                if(cacheStock == 0){
                    throw new GeneralException(ErrorStatus.OUT_OF_STOCK);
                }

                // 재고 차감
                int remaining = cacheStock - quantity;
                // Write-through: 캐시에 반영
                redisService.setValue(cacheKey, remaining);
                // DB에도 동기적으로 반영
                product.decreaseStock(quantity);
                productRepository.save(product);

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

        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            // 락 해제
            for (RLock l : locks) {
                if (l.isHeldByCurrentThread()) {
                    l.unlock();
                }
            }
        }

    }
}
