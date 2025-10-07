package com.ceos22.spring_boot.domain.order.repository;

import com.ceos22.spring_boot.domain.order.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {}