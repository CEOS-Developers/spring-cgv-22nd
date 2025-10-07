package com.ceos22.spring_boot.domain.movie.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.ceos22.spring_boot.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Genre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genreId;

    @Column(length = 20)
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private List<Categorizing> categorizings = new ArrayList<>();
}