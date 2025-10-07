package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.CreateReservationCommand;
import com.ceos22.cgv_clone.domain.dto.ReservationSummaryDto;
import com.ceos22.cgv_clone.domain.dto.SeatSelection;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final MemberRepository memberRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final ReservedSeatRepository reservedSeatRepository;
    private final PricingService pricingService;

    /** 예매 생성(+좌석 점유 HOLD) 및 티켓 스냅샷 생성 */
    @Transactional
    public ReservationSummaryDto createReservation(CreateReservationCommand cmd) {
        Member member = memberRepository.findById(cmd.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + cmd.memberId()));
        Screening screening = screeningRepository.findById(cmd.screeningId())
                .orElseThrow(() -> new IllegalArgumentException("상영회차 없음: " + cmd.screeningId()));

        if (cmd.selections() == null || cmd.selections().isEmpty()) {
            throw new IllegalArgumentException("선택된 좌석이 없습니다.");
        }

        // 좌석 로드 + 존재성 검증
        List<Long> seatIds = cmd.selections().stream().map(SeatSelection::seatId).toList();
        List<Seat> seats = seatRepository.findAllByIds(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 좌석 포함");
        }

        // 동일 상영회차의 좌석 점유 여부 검사
        long occupied = reservedSeatRepository.countOccupied(
                screening.getId(),
                seatIds,
                List.of(HoldStatus.HOLDING, HoldStatus.PAID)
        );
        if (occupied > 0) {
            throw new IllegalStateException("이미 선택된 좌석입니다.");
        }

        // 예매 엔티티 생성
        Reservation reservation = new Reservation(member, screening);

        // 좌석별 티켓 생성 + 좌석 점유(HOLD) 생성
        Map<Long, Seat> seatMap = seats.stream().collect(Collectors.toMap(Seat::getId, s -> s));
        for (SeatSelection sel : cmd.selections()) {
            Seat seat = seatMap.get(sel.seatId());

            int price = pricingService.unitPrice(screening, sel.ageGroup());
            Ticket ticket = new Ticket(seat, sel.ageGroup(), price);
            reservation.addTicket(ticket); // totalAmount 증가

            // 좌석 점유(HOLD) – 만료시간 (10분)
            ReservedSeat rs = new ReservedSeat(
                    screening,
                    seat,
                    reservation,
                    LocalDateTime.now().plusMinutes(10)
            );
            // 기본 holdStatus = HOLDING
            reservedSeatRepository.save(rs);
        }

        reservationRepository.save(reservation);
        return ReservationSummaryDto.from(reservation);
    }

    /** 결제 확정: 예매 상태 PAID, 좌석 점유도 PAID로 변경 */
    @Transactional
    public ReservationSummaryDto confirmPayment(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예매 없음: " + reservationId));
        if (r.getStatus() == ReservationStatus.PAID) {
            return ReservationSummaryDto.from(r);
        }

        // 점유 상태 전환
        List<ReservedSeat> holds = reservedSeatRepository.findAllByReservation(reservationId);
        holds.forEach(ReservedSeat::markPaid);

        r.markPaid();
        return ReservationSummaryDto.from(r);
    }

    /** 예매 취소 */
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("취소할 수 있는 예매 건이 존재하지 않습니다: " + reservationId));
        r.cancel();

        List<ReservedSeat> holds = reservedSeatRepository.findAllByReservation(reservationId);
        holds.forEach(ReservedSeat::markCancelled);
    }
}
