package com.ceos22.cgv_clone.domain.theater.entity;

import com.ceos22.cgv_clone.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region region;

    @Column(nullable = false)
    private String name;

    private String address;

    @Builder.Default
    @OneToMany(mappedBy = "theater")
    private List<Screen> screens = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "theater")
    private List<Store> stores = new ArrayList<>();
}
