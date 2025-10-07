package com.ceos22.cgv_clone.domain.theater.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AuditoriumType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audtype_id")
    private Long id;

    @Column(name = "is_special", nullable = false)
    private Boolean isSpecial;
}
