package com.ceos22.cgv_clone.domain.theater.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "uk_type_row_col", columnNames = {"audtype_id", "row_no", "column_no"})
)
@Getter
public class TypeSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "audtype_id", nullable = false)
    private AuditoriumType auditoriumType;

    @Column(name = "row_no", nullable = false)
    private Integer rowNo;

    @Column(name = "column_no", nullable = false)
    private Integer columnNo;
}