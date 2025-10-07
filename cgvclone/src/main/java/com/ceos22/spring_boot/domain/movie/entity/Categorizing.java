package com.ceos22.spring_boot.domain.movie.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Categorizing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long categorizingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;
}