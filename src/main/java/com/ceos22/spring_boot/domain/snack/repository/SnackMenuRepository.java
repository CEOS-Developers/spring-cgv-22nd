package com.ceos22.spring_boot.domain.snack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.SnackMenu;

public interface SnackMenuRepository extends JpaRepository<SnackMenu, Long> {
}
