package com.ceos22.spring_boot.domain.theater.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.ceos22.spring_boot.entity.ScreenSeat;

import jakarta.persistence.LockModeType;

public interface ScreenSeatRepository extends JpaRepository<ScreenSeat, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from ScreenSeat s where s.id = :id")
	Optional<ScreenSeat> findForUpdate(Long id);

	boolean existsByIdAndScreenId(Long id, Long screenId);
}
