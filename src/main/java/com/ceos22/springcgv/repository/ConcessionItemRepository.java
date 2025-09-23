package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.ConcessionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcessionItemRepository extends JpaRepository<ConcessionItem, Long> {
}