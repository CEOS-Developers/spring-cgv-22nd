package com.ceos22.cgv_clone.web.domain.reservation;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Embedded
    private final ReservationUuid reservationUuid;

    @Embedded
    private final ReservationTotalPrice reservationTotalPrice;

    @Embedded
    private final ReservationAmounts reservationAmounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private ReservationStatus reservationStatus;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservedSeat> reservedSeats = new ArrayList<>();

    public Reservation(User user, Schedule schedule, ReservationTotalPrice reservationTotalPrice, ReservationAmounts reservationAmounts) {

        this.user = user;
        this.schedule = schedule;
        this.reservationUuid = ReservationUuid.generate();
        this.reservationTotalPrice = reservationTotalPrice;
        this.reservationAmounts = reservationAmounts;
    }

    public static Reservation create(User user,
                                     Schedule schedule,
                                     ReservationTotalPrice reservationTotalPrice,
                                     ReservationAmounts reservationAmounts) {
        schedule.verifyNotStarted();
        reservationAmounts.validatePositive();
        reservationTotalPrice.validateNonNegative();
        return new Reservation(user, schedule, reservationTotalPrice, reservationAmounts);
    }

    public void cancel() {
        if (this.reservationStatus == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        this.reservationStatus = ReservationStatus.CANCELLED;
    }

    public List<String> reservedSeatNames() {
        return reservedSeats.stream()
                .map(rs -> rs.getSeat().getSeatName())
                .collect(Collectors.toList());
    }
}
