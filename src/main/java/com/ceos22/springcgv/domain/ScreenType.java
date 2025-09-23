package com.ceos22.springcgv.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "screen_types")
public class ScreenType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_type_id")
    private Long id;

    @Column(name = "type_name", unique = true, nullable = false, length = 50)
    private String typeName;

    @Column(name = "total_rows", nullable = false)
    private int totalRows;

    @Column(name = "total_cols", nullable = false)
    private int totalCols;
}