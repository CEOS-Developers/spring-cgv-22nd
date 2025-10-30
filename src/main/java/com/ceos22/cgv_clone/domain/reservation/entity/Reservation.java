package com.ceos22.cgv_clone.domain.reservation.entity;

import com.ceos22.cgv_clone.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Integer totalPrice;

    // 좌석별로 취소 불가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationSeat> reservationSeats = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Reservation(Member member, Schedule schedule, Integer totalPrice) {
        this.member = member;
        this.schedule = schedule;
        this.totalPrice = totalPrice;
        this.status = ReservationStatus.CONFIRMED;
    }

    public static Reservation create(Member member, Schedule schedule, Integer totalPrice) {
        return Reservation.builder()
                .member(member)
                .schedule(schedule)
                .totalPrice(totalPrice)
                .build();
    }

    public void addReservationSeat(ReservationSeat reservationSeat) {
        reservationSeats.add(reservationSeat);
        reservationSeat.setReservation(this);
    }
}
