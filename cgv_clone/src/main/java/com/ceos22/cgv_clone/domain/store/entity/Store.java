package com.ceos22.cgv_clone.domain.store.entity;

import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreType storeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    public static Store of(StoreType storeType, Theater theater) {
        return Store.builder()
                .storeType(storeType)
                .theater(theater)
                .build();
    }
}