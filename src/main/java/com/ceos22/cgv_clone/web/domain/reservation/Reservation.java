package com.ceos22.cgv_clone.web.domain.reservation;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.common.BaseEntity;
import com.ceos22.cgv_clone.web.domain.Payment;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.domain.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Embedded
    private ReservationUuid reservationUuid;

    @Embedded
    private ReservationTotalPrice reservationTotalPrice;

    @Embedded
    private ReservationAmounts reservationAmounts;

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

    @OneToOne(mappedBy = "reservation",cascade = CascadeType.ALL,orphanRemoval = true)
    private Payment payment;

    public Reservation(User user, Schedule schedule, ReservationTotalPrice reservationTotalPrice, ReservationAmounts reservationAmounts, ReservationStatus reservationStatus) {

        this.user = user;
        this.schedule = schedule;
        this.reservationUuid = ReservationUuid.generate();
        this.reservationTotalPrice = reservationTotalPrice;
        this.reservationAmounts = reservationAmounts;
        this.reservationStatus = reservationStatus;
    }

    public static Reservation create(User user,
                                     Schedule schedule,
                                     ReservationTotalPrice reservationTotalPrice,
                                     ReservationAmounts reservationAmounts) {
        schedule.verifyNotStarted();
        reservationAmounts.validatePositive();
        reservationTotalPrice.validateNonNegative();
        return new Reservation(user, schedule, reservationTotalPrice, reservationAmounts, ReservationStatus.RESERVED);
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

    public void verifyCancellable() {
        if (LocalDateTime.now().isAfter(schedule.getStartTime())){
            throw new GeneralException(ErrorStatus.RESERVTION_CANCEL_FAILED);
        }
    }
}
