package com.ceos22.spring_boot.domain.movie.entity;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.common.enums.Rating;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String title;

    private String language;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name="running_time")
    private Integer runningTime;

    @Column(name="release_date")
    private Date releaseDate;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name="is_active")
    @ColumnDefault("true")
    private Boolean isActive;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> directors;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> actors;

    @OneToMany(mappedBy = "movie")
    private List<Categorizing> categorizings = new ArrayList<>();
}
