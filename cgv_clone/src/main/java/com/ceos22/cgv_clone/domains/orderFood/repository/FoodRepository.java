package com.ceos22.cgv_clone.domains.orderFood.repository;

import com.ceos22.cgv_clone.domains.orderFood.domain.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
}
