package com.ceos22.cgv_clone.domain.shop.repository;


import com.ceos22.cgv_clone.domain.shop.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProduct_IdAndTheater_Id(Long productId, Long theaterId);
}
