package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreStockRepository extends JpaRepository<StoreStock, StoreStockId> {

    Optional<StoreStock> findByStoreAndProduct(Store store, Product product);

    @Query("SELECT ss FROM StoreStock ss WHERE ss.store = :store AND ss.product.category = :category")
    List<StoreStock> findByStoreAndProductCategory(@Param("store") Store store, @Param("category") ProductCategory category);

    boolean existsByStoreAndProduct(Store store, Product product);

    // 특정 매점에서 특정 상품을 특정 수량 이상 주문 가능한지 확인
    @Query("SELECT CASE WHEN COUNT(ss) > 0 THEN true ELSE false END FROM StoreStock ss " +
            "WHERE ss.store = :store AND ss.product = :product AND ss.stock >= :quantity")
    boolean existsOrderableStock(@Param("store") Store store, @Param("product") Product product, @Param("quantity") Integer quantity);
}
