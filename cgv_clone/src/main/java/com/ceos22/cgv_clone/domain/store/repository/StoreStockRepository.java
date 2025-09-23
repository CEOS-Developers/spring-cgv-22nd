package com.ceos22.cgv_clone.domain.store.repository;

import com.ceos22.cgv_clone.domain.store.entity.StoreStock;
import com.ceos22.cgv_clone.domain.store.entity.StoreStockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreStockRepository extends JpaRepository<StoreStock, StoreStockId> {
}
