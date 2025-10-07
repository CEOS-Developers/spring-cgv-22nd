package com.ceos22.cgv_clone.domain.booking.eneity;

import com.ceos22.cgv_clone.domain.common.enums.BookingStatus;
import com.ceos22.cgv_clone.domain.common.enums.PaymentType;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.screening.entity.Screening;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @Column(name = "booking_num", unique = true)
    private String bookingNum;

    @Column(name = "booking_at", nullable = false)
    private LocalDateTime bookingAt;

    @Column(name = "adult_count", nullable = false)
    private int adultCount;

    @Column(name = "teen_count", nullable = false)
    private int teenCount;

    @Column(name = "total_people", nullable = false)
    private int totalPeople;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    private Booking(Member member, Screening screening, LocalDateTime bookingAt,
                    PaymentType paymentType, int adultCount, int teenCount,
                    int totalPeople, int totalPrice) {
        this.member = member;
        this.screening = screening;
        this.bookingAt = bookingAt;
        this.paymentType = paymentType;
        this.status = BookingStatus.BOOKED;
        this.adultCount = adultCount;
        this.teenCount = teenCount;
        this.totalPeople = totalPeople;
        this.totalPrice = totalPrice;
    }

    public static Booking create(Member member, Screening screening, PaymentType paymentType,
                                 int adultCount, int teenCount, int totalPrice) {
        int people = adultCount + teenCount;
        return new Booking(member, screening, LocalDateTime.now(), paymentType,
                adultCount, teenCount, people, totalPrice);
    }

    public void cancel() {
        this.status = BookingStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }
}
