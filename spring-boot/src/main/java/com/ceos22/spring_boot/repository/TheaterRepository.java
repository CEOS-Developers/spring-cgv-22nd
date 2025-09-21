package com.ceos22.spring_boot.repository;

import com.ceos22.spring_boot.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

}

