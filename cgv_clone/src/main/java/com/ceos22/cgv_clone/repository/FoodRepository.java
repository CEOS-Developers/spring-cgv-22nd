package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.orderFood.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
}
