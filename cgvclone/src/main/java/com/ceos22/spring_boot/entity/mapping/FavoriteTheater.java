package com.ceos22.spring_boot.entity.mapping;

import com.ceos22.spring_boot.common.BaseEntity;
import com.ceos22.spring_boot.entity.Theater;
import com.ceos22.spring_boot.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FavoriteTheater extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ftId;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;}
