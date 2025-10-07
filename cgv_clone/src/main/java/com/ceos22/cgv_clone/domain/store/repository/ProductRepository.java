package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.ProductCategory;
import com.ceos22.cgv_clone.domain.store.entity.StoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT ss.product FROM StoreStock ss WHERE ss.store.storeType = :storeType AND ss.product.category = :category AND ss.product.isActive = true")
    List<Product> findByStoreTypeAndCategory(@Param("storeType") StoreType storeType, @Param("category") ProductCategory category);
}
