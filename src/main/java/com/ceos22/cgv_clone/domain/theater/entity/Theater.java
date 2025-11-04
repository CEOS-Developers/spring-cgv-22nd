package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theater")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String accessInfo;

    @Column(columnDefinition = "TEXT")
    private String parkingInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TheaterRegion region; // 서울/경기/...

    @Builder
    public Theater(String name, String address, String accessInfo, String parkingInfo, TheaterRegion region) {
        this.name = name;
        this.address = address;
        this.accessInfo = accessInfo;
        this.parkingInfo = parkingInfo;
        this.region = region;
    }
}
