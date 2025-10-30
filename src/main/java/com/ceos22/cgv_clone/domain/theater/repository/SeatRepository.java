package com.ceos22.cgv_clone.domain.theater.repository;

import com.ceos22.cgv_clone.domain.theater.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // 좌석 배치도 조회용
    @Query("""
        SELECT s from Seat s
        WHERE s.auditorium.id = :auditoriumId
        ORDER BY s.rowNo ASC, s.columnNo ASC
    """)
    List<Seat> findAllByAuditoriumIdSorted(Long auditoriumId);


    @Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findAllByIdWithLock(@Param("ids") List<Long> ids);
}
