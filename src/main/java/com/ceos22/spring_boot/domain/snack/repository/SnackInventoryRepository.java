package com.ceos22.spring_boot.domain.snack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.SnackInventory;

public interface SnackInventoryRepository extends JpaRepository<SnackInventory, Long> {
	Optional<SnackInventory> findByTheaterIdAndSnackMenuId(Long theaterId, Long snackMenuId);
}
