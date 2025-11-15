package com.ceos22.cgv_clone.domain.reservation.entity;

import com.ceos22.cgv_clone.global.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    // 정규화 측면에서는 불필요하나, 서비스 로직 간편화 측면에서 이용. (showtime 정보를 ticket.getShotime()으로 접근 가능)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "general_cnt")
    private int generalCnt;

    @Column(name = "youth_cnt")
    private int youthCnt;

    @Column(name = "final_price")
    private int finalPrice;

    public static Ticket of(Showtime showtime, User user, int generalCount, int youthCount, int finalPrice) {
        Ticket ticket = new Ticket();
        ticket.showtime = showtime;
        ticket.user = user;
        ticket.generalCnt = generalCount;
        ticket.youthCnt = youthCount;
        ticket.finalPrice = finalPrice;
        return ticket;
    }

}

