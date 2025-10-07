package com.ceos22.spring_boot.domain.theater.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.Region;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "theaters", indexes = {
        @Index(name = "idx_theater_id", columnList = "theater_id")
})
public class Theater extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    @Column(name = "theater_name", nullable = false, length = 50, unique = true)
    private String theaterName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Region region;

    @Column(nullable = false, length = 100)
    private String address;

    private String contact;

}