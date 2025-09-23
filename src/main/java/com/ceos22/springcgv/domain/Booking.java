package com.ceos22.springcgv.domain;

import com.ceos22.springcgv.domain.common.BaseEntity;
import com.ceos22.springcgv.domain.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showing_id", nullable = false)
    private Showing showing;

    @Enumerated(EnumType.STRING)  // BOOKED, CANCELED
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Builder
    public Booking(User user, Showing showing, BigDecimal totalPrice, BookingStatus status) {
        this.user = user;
        this.showing = showing;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    //== 비즈니스 로직 ==//
    public void cancel() {
        this.status = BookingStatus.CANCELED;
    }
}