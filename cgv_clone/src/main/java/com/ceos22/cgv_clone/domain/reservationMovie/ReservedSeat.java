package com.ceos22.cgv_clone.domain.reservationMovie;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reserved_seat",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reservedseat_screening_seat",
                        columnNames = {"screening_id", "seat_id"}
                )
        }
)
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservedSeat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserved_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening; // 상영 회차

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat; // 좌석

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation; // 예약

    @Enumerated(EnumType.STRING)
    @Column(name = "hold_status", nullable = false, length = 20)
    private HoldStatus holdStatus = HoldStatus.HOLDING; // 점유 상태

    private LocalDateTime expireAt; // 점유 만료 시간 -> 예매 도중 결제 안하면 해제

    //== 생성 메서드 ==//
    public ReservedSeat(Screening screening, Seat seat, Reservation reservation, LocalDateTime expireAt) {
        this.screening = screening;
        this.seat = seat;
        this.reservation = reservation;
        this.expireAt = expireAt;
        this.holdStatus = HoldStatus.HOLDING;
    }

    //== 비즈니스 로직 ==//
    public void markPaid() {
        this.holdStatus = HoldStatus.PAID;
        this.expireAt = null; // 결제 완료 시
    }

    public void markCancelled() {
        this.holdStatus = HoldStatus.CANCELLED;
    }

    public boolean isExpired() {
        return expireAt != null && LocalDateTime.now().isAfter(expireAt);
    }
}
