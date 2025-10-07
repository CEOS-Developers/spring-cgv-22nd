package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.ProductOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderItemRepository extends JpaRepository<ProductOrderItem, Long> {

    List<ProductOrderItem> findByProductOrderId(Long orderId);
}
