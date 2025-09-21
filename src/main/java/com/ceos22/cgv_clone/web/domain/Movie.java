package com.ceos22.cgv_clone.web.domain;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.enums.AgeRating;
import com.ceos22.cgv_clone.web.domain.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private LocalDate releaseDate;

    private Integer runningTime;

    @Column(length = 50)
    private String poster;

    @Column(length = 10)
    private String genre;

    @Column(columnDefinition = "text")
    private String prolog;

    @Enumerated(EnumType.STRING)
    private Region region;

    private AgeRating ageRating;

}
