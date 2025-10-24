package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.service.RedisService;
import com.ceos22.cgv_clone.web.domain.*;
import com.ceos22.cgv_clone.web.domain.purchase.Purchase;
import com.ceos22.cgv_clone.web.domain.purchase.Quantity;
import com.ceos22.cgv_clone.web.domain.purchase.Price;
import com.ceos22.cgv_clone.web.dto.PurchaseRequestDto;
import com.ceos22.cgv_clone.web.dto.PurchaseResponseDto;
import com.ceos22.cgv_clone.web.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CinemaService cinemaService;
    private final ProductService productService;
    private final RedissonClient redissonClient;
    private final RedisService redisService;
    private final List<RLock> heldLocks;
    private final PaymentService paymentService;

    @Transactional
    public PurchaseResponseDto purchase(PurchaseRequestDto purchaseRequestDto, User user) {
        Cinema cinema = cinemaService.getCinemaById(purchaseRequestDto.getCinemaId());

        //구매 요청한 상품 ID목록
        List<Long> productIds = purchaseRequestDto.getProducts().stream()
                .map(PurchaseRequestDto.ProductDto::getProductId)
                .collect(Collectors.toList());

        //해당 상품들에 대한 분산 락 획득
        lockAll(productIds);
        Payment payment = null;
        Map<Long, Integer> decrementedStock = new HashMap<>();
        try{
            List<Product> products = productService.getProductsById(productIds);
            List<PurchaseRequestDto.ProductDto> productDtos = purchaseRequestDto.getProducts();

            //재고가 충분한지 확인
            productDtos.forEach(productDto -> {
                Product product = productService.findProduct(products, productDto.getProductId());
                int available = getStock(product.getId());
                if (productDto.getQuantity() > available){
                    throw new GeneralException(ErrorStatus.OUT_OF_STOCK);
                }
            });

            //결제 로직 추가
            log.debug("Processing payment for userId={}, products={}", user.getId(), productIds);
            payment = paymentService.processPurchasePayment(products, purchaseRequestDto.getProducts());
            log.info("Payment succeeded: paymentId={}", payment.getId());

            Purchase purchase = createPurchase(user,cinema,productDtos,products,decrementedStock);
            purchaseRepository.save(purchase);

            return PurchaseResponseDto.of(purchase);

        } catch(Exception ex) {
            log.error("Unexpected error during purchase: userId={}, products={}, decrementedStock={}, paymentId={}",
                    user.getId(), productIds, decrementedStock, payment != null ? payment.getId() : null, ex);

            if (payment != null) {
                try {
                    paymentService.cancelPayment(payment.getId());
                } catch(Exception cancelEx) {
                    log.error("Payment cancellation failed for paymentId: {}",
                            payment.getPaymentUuid(), cancelEx);
                }
            }

            rollbackStock(decrementedStock);
            throw new GeneralException(ErrorStatus.PAYMENT_FAILED);
        } finally {
            //락 해제
            unlockAll();
        }
    }

    //각 상품 ID에 대해 락 획득 시도 및 성공 시 heldLocks에 저장
    public void lockAll(List<Long> productIds) {
        productIds.forEach(id -> {
            String key = "productLock: " + id;
            RLock lock = redissonClient.getLock(key);
            try {
                if (!lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                    throw new GeneralException(ErrorStatus.PRODUCT_LOCK_FAILED);
                }
                heldLocks.add(lock);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new GeneralException(ErrorStatus.PRODUCT_LOCK_FAILED);
            } catch (Exception e) {
                unlockAll();
                throw e;
            }
        });
    }

    public Purchase createPurchase(User user,
                                   Cinema cinema,
                                   List<PurchaseRequestDto.ProductDto> purchaseProducts,
                                   List<Product> products,
                                   Map<Long, Integer> decrementedStock) {
        Purchase purchase = Purchase.create(user, cinema);
        Price totalPrice = Price.of(0);
        Quantity totalQuantity = Quantity.of(0);

        // 각 요청된 상품에 대해 재고 감소, 가격 계산
        for (PurchaseRequestDto.ProductDto dto : purchaseProducts) {
            Product product = productService.findProduct(products, dto.getProductId());
            Quantity quantity = Quantity.of(dto.getQuantity());
            Price unitPrice = Price.of(product.getPrice()).multiply(quantity);

            int currentStock = product.getStock();
            product.decreaseStock(quantity.value());
            decrementedStock.put(product.getId(), currentStock);

            markStockUpdate(product.getId(), product.getStock());

            purchase.addProduct(product, quantity, unitPrice);

            totalQuantity = totalQuantity.add(quantity);
            totalPrice = totalPrice.add(unitPrice);
        }

        // 총 수량과 총 가격을 설정하여 완료
        purchase.setTotal(totalQuantity, totalPrice);
        return purchase;
    }

    // 획득된 락 중 현재 스레드가 가지고 있는 락을 모두 해제하고 리스트 초기화
    public void unlockAll() {
        heldLocks.stream()
                .filter(RLock::isHeldByCurrentThread)
                .forEach(RLock::unlock);
        heldLocks.clear();
    }

    // 캐시에서 재고 조회, 없으면 DB 조회 후 캐시에 설정
    public int getStock(Long productId) {
        String key = "product:stock:" + productId;
        Object raw = redisService.getValue(key);
        Integer stock = null;

        if (raw instanceof Integer) {
            stock = (Integer) raw;
        } else if (raw instanceof String) {
            try {
                stock = Integer.valueOf((String) raw);
            } catch (NumberFormatException e) {
                log.warn("Invalid stock value in cache for key {}: {}", key, raw);
            }
        } else if (raw instanceof Long) {
            stock = ((Long) raw).intValue();
        }

        if (stock == null) {
            Product product = productService.getProductById(productId);
            stock = product.getStock();
            redisService.setValue(key, stock);
        }
        return stock;
    }

    // 상품 재고 캐시에 저장 (write-through)
    public void markStockUpdate(Long productId, int newStock) {
        String key = "product:stock:" + productId;
        redisService.setValue(key, newStock);
    }

    private void rollbackStock(Map<Long, Integer> decrementedStock) {
        decrementedStock.forEach((productId, originalStock) -> {
            try {
                Product product = productService.getProductById(productId);
                product.setStock(originalStock);
                markStockUpdate(productId, originalStock);
            } catch (Exception e) {
                log.error("Failed to rollback stock for product: {}", productId, e);
            }
        });
    }
}
