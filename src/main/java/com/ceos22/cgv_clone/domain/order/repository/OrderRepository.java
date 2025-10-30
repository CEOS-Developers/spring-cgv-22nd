package com.ceos22.cgv_clone.domain.order.repository;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMember(Member member);

    Optional<List<Order>> findByMemberAndStatus(Member member, OrderStatus orderStatus);
}
