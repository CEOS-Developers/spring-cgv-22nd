package com.ceos22.cgv_clone.domain.store.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.store.dto.request.StoreCreateRequest;
import com.ceos22.cgv_clone.domain.store.dto.request.StoreStockCreateRequest;
import com.ceos22.cgv_clone.domain.store.dto.response.StoreProductResponse;
import com.ceos22.cgv_clone.domain.store.dto.response.StoreResponse;
import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import com.ceos22.cgv_clone.domain.store.entity.Store;
import com.ceos22.cgv_clone.domain.store.entity.StoreStock;
import com.ceos22.cgv_clone.domain.store.repository.ProductRepository;
import com.ceos22.cgv_clone.domain.store.repository.StoreRepository;
import com.ceos22.cgv_clone.domain.store.repository.StoreStockRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreStockRepository storeStockRepository;
    private final ProductRepository productRepository;
    private final TheaterRepository theaterRepository;

    public List<StoreResponse> findStoresByTheater(Long theaterId) {

        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        List<Store> stores = storeRepository.findByTheater(theater);

        return stores.stream()
                .map(StoreResponse::from)
                .collect(Collectors.toList());
    }

    public List<StoreProductResponse> findProductsByStoreAndCategory(Long storeId, ProductCategory category) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        List<StoreStock> storeStocks = storeStockRepository.findByStoreAndProductCategory(store, category);

        return storeStocks.stream()
                .map(StoreProductResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreResponse createStore(StoreCreateRequest request) {

        Theater theater = theaterRepository.findById(request.theaterId())
                .orElseThrow(() -> new CustomException(ErrorCode.THEATER_NOT_FOUND));

        Store store = Store.of(request.storeType(), theater);
        Store savedStore = storeRepository.save(store);

        return StoreResponse.from(savedStore);
    }

    @Transactional
    public void addProductToStore(StoreStockCreateRequest request) {

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (storeStockRepository.existsByStoreAndProduct(store, product)) {
            throw new CustomException(ErrorCode.DUPLICATED_RESOURCE);
        }

        StoreStock storeStock = StoreStock.of(store, product, request.initialStock());
        storeStockRepository.save(storeStock);
    }

    @Transactional
    public void increaseStock(Long storeId, Long productId, Integer quantity) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        StoreStock storeStock = storeStockRepository.findByStoreAndProduct(store, product)
                .orElseThrow(() -> new CustomException(ErrorCode.STOCK_NOT_FOUND));

        storeStock.increaseStock(quantity);
    }

    @Transactional
    public void decreaseStock(Long storeId, Long productId, Integer quantity) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        StoreStock storeStock = storeStockRepository.findByStoreAndProduct(store, product)
                .orElseThrow(() -> new CustomException(ErrorCode.STOCK_NOT_FOUND));

        storeStock.decreaseStock(quantity);
    }

}
