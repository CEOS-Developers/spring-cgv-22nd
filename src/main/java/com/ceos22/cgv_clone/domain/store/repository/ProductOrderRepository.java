package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.ProductOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    // 회원별 주문 목록
    List<ProductOrder> findByMemberIdOrderByIdDesc(Long memberId);

    // 주문 상세 조회 (단건)
    @Query("""
        SELECT po FROM ProductOrder po
        JOIN FETCH po.member
        JOIN FETCH po.cinema
        WHERE po.id = :id
    """)
    Optional<ProductOrder> findDetailsById(@Param("id") Long id);
}
