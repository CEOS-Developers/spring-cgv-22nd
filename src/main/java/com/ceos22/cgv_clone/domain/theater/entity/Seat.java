package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.domain.common.enums.SeatType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_aud_row_col", columnNames = {"auditorium_id", "row_no", "column_no"})
)
@Getter
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "auditorium_id", nullable = false)
    private Auditorium auditorium;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SeatType type;

    @Column(name = "row_no", nullable = false)
    private Integer rowNo;

    @Column(name = "column_no", nullable = false)
    private Integer columnNo;
}