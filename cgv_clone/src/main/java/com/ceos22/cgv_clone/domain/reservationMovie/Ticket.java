package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name="ticket",
        uniqueConstraints=@UniqueConstraint(name="uk_ticket_res_seat", columnNames={"reservation_id","seat_id"}))
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id", nullable=false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seat_id", nullable=false)
    private Seat seat;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column(nullable=false)
    private int unitPrice; // 스냅샷

    //== 생성 메서드==//
    public Ticket(Seat seat, AgeGroup ageGroup, int unitPrice) {
        this.seat = seat; this.ageGroup = ageGroup; this.unitPrice = unitPrice;
    }

    void setReservation(Reservation r){ this.reservation = r; }

}
