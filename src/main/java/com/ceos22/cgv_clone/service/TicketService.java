package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.User;
import com.ceos22.cgv_clone.domain.reservation.*;
import com.ceos22.cgv_clone.domain.theater.Showtime;
import com.ceos22.cgv_clone.dto.request.TicketRequestDto;
import com.ceos22.cgv_clone.dto.response.TicketResponseDto;
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
    private final ReservationSeatRepository reservationSeatRepository;


    // 예매
    @Transactional
    public TicketResponseDto reserveTicket(String username, TicketRequestDto request) {

        // showtime, user 가져오기
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(()-> new IllegalArgumentException("상영시간표 없음"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 최종 결제 금액 계산
        int finalPrice = request.getGeneralCount() * TicketPrice.GENERAL.getPrice()
                + request.getYouthCount() * TicketPrice.YOUTH.getPrice();

        // 좌석 예매
        List<ReservationSeat> reservedSeats = new ArrayList<>();

        //예약된 좌석인지 확인
        for (String seatNumber : request.getSeatNumbers()) {
            String row = seatNumber.substring(0, 1);
            String col = seatNumber.substring(1);

            boolean alreadyReserved = reservationSeatRepository
                    .existsByShowtime_IdAndSeatRowAndSeatColAndStatus(request.getShowtimeId(),row, col, ReservationStatus.RESERVED);

            if (alreadyReserved) {
                throw new IllegalStateException("이미 예약된 좌석입니다.");
            }

            // 없거나 AVAILABLE 상태라면 RESERVED로 저장
            ReservationSeat newReservation = ReservationSeat.builder()
                    .showtime(showtime)
                    .seatRow(row)
                    .seatCol(col)
                    .status(ReservationStatus.RESERVED)
                    .build();

            reservedSeats.add(reservationSeatRepository.save(newReservation));
        }

        // Ticket 저장
        Ticket ticket = Ticket.builder()
                .showtime(showtime)
                .user(user)
                .generalCnt(request.getGeneralCount())
                .youthCnt(request.getYouthCount())
                .finalPrice(finalPrice)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        // 좌석에 티켓 연결
        for (ReservationSeat rs : reservedSeats) {
            rs.setTicket(savedTicket);
        }

        List<String> rsSeats = reservedSeats
                .stream()
                .map(rs -> rs.getSeatRow() + rs.getSeatCol())
                .toList();

        return TicketResponseDto.fromEntity(savedTicket, rsSeats);
    }

    // 예매 취소
    @Transactional
    public void cancelTicket(String username, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인이 예매한 티켓만 취소할 수 있습니다.");
        }

        // 티켓과 연결된 좌석 삭제
        reservationSeatRepository.deleteByTicket_Id(ticket.getId());

        // 티켓 삭제
        ticketRepository.delete(ticket);
    }
}