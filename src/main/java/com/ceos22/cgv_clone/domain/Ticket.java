package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "general_cnt")
    private int generalCnt;

    @Column(name = "youth_cnt")
    private int youthCnt;

    @Column(name = "final_price")
    private int finalPrice;

    private LocalDateTime createdAt;
}

