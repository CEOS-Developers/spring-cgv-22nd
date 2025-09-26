package com.ceos22.cgv.module.snack.repository;

import com.ceos22.cgv.module.snack.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);

    @Query("""
        select o from Order o
        left join fetch o.items i
        left join fetch i.menu
        where o.id = :orderId
    """)
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);
}
