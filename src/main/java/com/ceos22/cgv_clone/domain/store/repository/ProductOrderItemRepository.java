package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.ProductOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOrderItemRepository extends JpaRepository<ProductOrderItem, Long> {

    // 주문 아이템을 찾을 때 Product 정보도 JOIN FETCH 같이
    @Query("""
        SELECT poi FROM ProductOrderItem poi
        JOIN FETCH poi.product
        WHERE poi.productOrder.id = :orderId
    """)
    List<ProductOrderItem> findByProductOrderIdWithProduct(@Param("orderId") Long orderId);

    List<ProductOrderItem> findByProductOrderId(Long orderId);
}
