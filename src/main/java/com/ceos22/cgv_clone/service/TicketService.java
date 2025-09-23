package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.*;
import com.ceos22.cgv_clone.dto.ticket.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    // 티켓 가격 하드코딩
    private static final int GENERAL_PRICE = 12000;
    private static final int YOUTH_PRICE = 10000;

    // 예매
    @Transactional
    public TicketResponseDto reserveTicket(TicketRequestDto request) {
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 최종 결제 금액 계산
        int finalPrice = request.getGeneralCount() * GENERAL_PRICE
                + request.getYouthCount() * YOUTH_PRICE;

        // Ticket 생성
        Ticket ticket = Ticket.builder()
                .showtime(showtime)
                .user(user)
                .generalCnt(request.getGeneralCount())
                .youthCnt(request.getYouthCount())
                .finalPrice(finalPrice)
                .createdAt(LocalDateTime.now())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        // 좌석 예매
        List<String> reservedSeats = new ArrayList<>();
        for (Long seatId : request.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new IllegalArgumentException("좌석 없음"));

            // 이미 예약된 좌석인지 확인
            if (reservationSeatRepository.existsByShowtime_IdAndSeat_IdAndStatus(
                    showtime.getId(), seatId, ReservationStatus.RESERVED)) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }

            ReservationSeat reservationSeat = ReservationSeat.builder()
                    .seat(seat)
                    .showtime(showtime)
                    .ticket(savedTicket)
                    .status(ReservationStatus.RESERVED)
                    .build();

            reservationSeatRepository.save(reservationSeat);
            reservedSeats.add(seat.getSeat_row() + seat.getSeat_col());
        }

        return TicketResponseDto.builder()
                .ticketId(savedTicket.getId())
                .showtimeId(showtime.getId())
                .generalCount(savedTicket.getGeneralCnt())
                .youthCount(savedTicket.getYouthCnt())
                .finalPrice(savedTicket.getFinalPrice())
                .reservedSeats(reservedSeats)
                .build();
    }

    // 예매 취소
    @Transactional
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));

        // 좌석 상태 되돌리기
        List<ReservationSeat> reservedSeats = reservationSeatRepository.findByTicket(ticket);
        for (ReservationSeat rs : reservedSeats) {
            rs.setStatus(ReservationStatus.AVAILABLE);
        }

        // reservation_seat 먼저 삭제
        reservationSeatRepository.deleteByTicket(ticket);

        ticketRepository.delete(ticket);
    }
}