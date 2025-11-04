package com.ceos22.cgv_clone.domains.reservationMovie.service;

import com.ceos22.cgv_clone.api.dto.CreateReservationCommand;
import com.ceos22.cgv_clone.api.dto.PaymentResponse;
import com.ceos22.cgv_clone.api.dto.ReservationSummaryDto;
import com.ceos22.cgv_clone.api.dto.SeatSelection;
import com.ceos22.cgv_clone.domains.member.adaptor.MemberReader;
import com.ceos22.cgv_clone.domains.orderFood.adaptor.CinemaNameReader;
import com.ceos22.cgv_clone.domains.orderFood.dto.InstantPaymentRequest;
import com.ceos22.cgv_clone.domains.reservationMovie.adaptor.ScreeningReader;
import com.ceos22.cgv_clone.domains.reservationMovie.domain.*;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.*;
import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final MemberReader memberReader;
    private final ScreeningReader screeningReader;
    private final CinemaNameReader cinemaNameReader;
    private final PricingService pricingService;
    private final TicketRepository ticketRepository;


    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final RedissonClient redissonClient;
    private final PaymentService paymentService;



    /** 예매 생성(+좌석 점유 HOLD) 및 티켓 스냅샷 생성 */
    @Transactional
    public ReservationSummaryDto createReservation(CreateReservationCommand cmd) {

        // DTO에 담긴 member, screening 검증
        memberReader.getById(cmd.memberId());
        screeningReader.getScreeningById(cmd.screeningId());

        if (cmd.selections() == null || cmd.selections().isEmpty()) {
            throw new IllegalArgumentException("선택된 좌석이 없습니다.");
        }

        // 좌석 로드 + 존재성 검증
        List<Long> seatIds = cmd.selections().stream().map(SeatSelection::seatId).toList();
        List<Seat> seats = seatRepository.findAllByIds(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 좌석 포함");
        }

        // 2) 좌석 멀티락 (screeningId+seatId 단위)
        List<RLock> seatLocks = seatIds.stream()
                .map(id -> redissonClient.getLock("LOCK:SEAT:" + cmd.screeningId() + ":" + id))
                .toList();
        RedissonMultiLock multiLock = new RedissonMultiLock(seatLocks.toArray(new RLock[0]));
        boolean locked = false;

        try {
            locked = multiLock.tryLock(2, 300, TimeUnit.SECONDS); // 대기 2초, 보유 300초
            if (!locked) throw new IllegalStateException("다른 사용자가 좌석을 선점 중입니다. 잠시 후 다시 시도하세요.");

            // 3) 동일 상영회차 좌석 점유 여부 다시 확인 (HOLDING/PAID)
            long occupied = reservedSeatRepository.countOccupied(
                    cmd.screeningId(),
                    seatIds,
                    List.of(HoldStatus.HOLDING, HoldStatus.PAID)
            );
            if (occupied > 0) throw new IllegalStateException("이미 선택된 좌석이 있습니다.");

            // 4) 예매 엔티티 생성
            Reservation reservation = new Reservation(cmd.memberId(), cmd.screeningId());

            Map<Long, Seat> seatMap = seats.stream().collect(Collectors.toMap(Seat::getId, s -> s));
            LocalDateTime now = LocalDateTime.now();
            for (SeatSelection sel : cmd.selections()) {
                Seat seat = seatMap.get(sel.seatId());
                int price = pricingService.unitPrice(sel.ageGroup());

                Ticket ticket = new Ticket(seat, sel.ageGroup(), price);
                reservation.addTicket(ticket); // totalAmount 증가

                // 좌석 점유(HOLD) – 10분 만료
                ReservedSeat rs = new ReservedSeat(
                        cmd.screeningId(),
                        seat,
                        reservation,
                        now.plusMinutes(10)
                ); // 기본 HOLDING
                reservedSeatRepository.save(rs);
            }

            reservationRepository.save(reservation);
            return ReservationSummaryDto.from(reservation);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("좌석 락 대기 중 인터럽트", e);
        } finally {
            if (locked) multiLock.unlock();
        }
    }

    /** 결제 시도 및 확정: 성공 시 예매/좌석 점유 상태를 PAID로 전환 (10% 실패 반영) */
    @Transactional
    public ReservationSummaryDto payReservation(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예매 없음: " + reservationId));
        if (r.getStatus() == ReservationStatus.PAID) {
            return ReservationSummaryDto.from(r); // 이미 결제됨
        }

        // 결제 요청 생성
        String paymentId = "Reservation-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8);

        int totalAmount = r.getTotalAmount();
        String orderName = "Screening#" + r.getScreeningId() + " x " + r.getTickets().size();

        InstantPaymentRequest req = new InstantPaymentRequest(
                "{\"screening\":" + r.getScreeningId().toString() + "}",
                orderName,
                totalAmount,
                "KRW",
                "{\"reservationId\":" + reservationId + "}"
        );

        try {
            PaymentResponse pr = paymentService.pay(paymentId, req);

            // 성공 → 좌석 HOLD를 PAID로, 예약도 PAID
            List<ReservedSeat> holds = reservedSeatRepository.findAllByReservation(reservationId);
            holds.forEach(ReservedSeat::markPaid);

            r.markPaid();
            r.confirmReservationPayment(paymentId, orderName, req.currency());

            return ReservationSummaryDto.from(r);

        } catch (WebClientResponseException ex) {
            // 결제 실패(10% 500 등) → 예약/좌석 점유 취소 & 재고/좌석 복원
            cancelReservationInternal(r);
            throw new IllegalStateException("결제 실패");
        } catch (RuntimeException ex) {
            cancelReservationInternal(r);
            throw ex;
        }
    }

    /** 예매 취소 (사용자/만료) */
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("취소할 수 있는 예매가 없습니다: " + reservationId));
        cancelReservationInternal(r);
    }

    private void cancelReservationInternal(Reservation r) {
        r.cancel(); // 예약 상태 CANCELLED
        List<ReservedSeat> holds = reservedSeatRepository.findAllByReservation(r.getId());
        holds.forEach(ReservedSeat::markCancelled); // 점유 해제
    }
}
