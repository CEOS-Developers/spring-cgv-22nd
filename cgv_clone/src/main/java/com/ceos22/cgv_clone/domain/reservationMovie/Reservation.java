package com.ceos22.cgv_clone.domain.reservationMovie;


import com.ceos22.cgv_clone.domain.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="reservation")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name="member_id")
    private Long memberId; // 주문 회원

    @Column(name="screening_id", nullable=false)
    private Long screeningId; // 상영 회차

    @Column(nullable=false)
    private LocalDateTime reservedAt = LocalDateTime.now(); // 예매 시간

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CREATED; // 예매 상태

    @Column(nullable=false)
    private int totalAmount; // 스냅샷 합계

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

    //==연관관계 메서드==//
    public Reservation(Long memberId, Long screeningId) {
        this.memberId = memberId; this.screeningId = screeningId;
    }

    public void addTicket(Ticket t){
        tickets.add(t);
        t.setReservation(this);
        totalAmount += t.getUnitPrice();
    }

    public void markPaid(){ this.status = ReservationStatus.PAID; }
    public void cancel(){ this.status = ReservationStatus.CANCELLED; }
}
