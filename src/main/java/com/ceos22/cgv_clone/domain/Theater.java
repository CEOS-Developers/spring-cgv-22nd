package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "access_info", length = 1000)
    private String accessInfo;

    @Column(name = "parking_info", length = 1000)
    private String parkingInfo;

    @Column(nullable = false, length = 50)
    private String region;
}
