package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.reservation.entity.TicketPrice;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.TicketRepository;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import com.ceos22.cgv_clone.domain.reservation.entity.Ticket;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.request.TicketRequestDto;
import com.ceos22.cgv_clone.domain.reservation.dto.response.TicketResponseDto;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentRecord;
import com.ceos22.cgv_clone.domain.payment.PaymentRecordRepository;
import com.ceos22.cgv_clone.domain.payment.service.PaymentService;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentStatus;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentTarget;
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ceos22.cgv_clone.global.redis.RedissonLockHelper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final RedissonLockHelper lockHelper;
    private final PaymentService paymentService;
    private final PaymentRecordRepository paymentRecordRepository;

    /* -------------서비스 로직(예매, 예매 취소, 결제)----------*/
    // 예매
    @Transactional
    public TicketResponseDto reserveTicket(String username, TicketRequestDto request) {

        User user = getUser(username);
        Showtime showtime = getShowtime(request.getShowtimeId());
        int finalPrice = calcFinalPrice(request);

        // 이번 예약의 좌석 키들을 수집
        List<String> lockKeys = request.getSeatNumbers().stream()
                .map(seat -> {
                    String row = seat.substring(0, 1);
                    String col = seat.substring(1);
                    return seatKey(request.getShowtimeId(), row, col);
                })
                .distinct()
                .toList();

        // 잠금(lock) 실행
        return lockHelper.withLocks(lockKeys, 2000, 5000, () -> {
            // 좌석 예매
            List<ReservationSeat> reservedSeats = new ArrayList<>();

            for (String seatNumber : request.getSeatNumbers()) {
                String[] rc = parseSeat(seatNumber);

                boolean alreadyReserved = reservationSeatRepository
                        .existsByShowtime_IdAndSeatRowAndSeatColAndStatus(request.getShowtimeId(), rc[0], rc[1], ReservationStatus.RESERVED);

                if (alreadyReserved) {
                    throw new BusinessException(ErrorCode.SEAT_ALREADY_RESERVED,
                            "이미 예약된 좌석입니다.(showtimeId=%d seat=%s)".formatted(request.getShowtimeId(), seatNumber));
                }

                ReservationSeat newReservation = ReservationSeat.builder()
                        .showtime(showtime)
                        .seatRow(rc[0])
                        .seatCol(rc[1])
                        .status(ReservationStatus.RESERVED)
                        .build();

                reservedSeats.add(reservationSeatRepository.save(newReservation));
            }

            // Ticket 저장
            Ticket ticket = Ticket.of(showtime, user, request.getGeneralCount(), request.getYouthCount(), finalPrice);
            Ticket savedTicket = ticketRepository.save(ticket);

            // 좌석에 티켓 연결
            for (ReservationSeat rs : reservedSeats) {
                rs.setTicket(savedTicket);
            }

            // 결제 내역 레코드에 REQUESTED 상태로 저장
            String paymentId = paymentIdForTicket(savedTicket.getId());
            paymentRecordRepository.save(PaymentRecord.createTicket(savedTicket.getId(), paymentId));

            // 응답 생성
            List<String> rsSeats = reservedSeats.stream().map(rs -> rs.getSeatRow() + rs.getSeatCol()).toList();

            return TicketResponseDto.fromEntity(savedTicket, rsSeats);
        });


    }

    @Transactional
    public void cancelTicket(String username, Long ticketId) {
        Ticket ticket = getTicketOwned(username, ticketId);

        // 최신 결제 레코드 조회
        PaymentRecord pr = paymentRecordRepository
                .findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.TICKET, ticketId)
                .orElse(null);

        // 상태별 처리
        if (pr != null) {
            switch (pr.getStatus()) {
                case PAID -> {
                    // PG 결제 취소 호출
                    try {
                        paymentService.cancel(pr.getPaymentId());
                        pr.setStatus(PaymentStatus.CANCELLED);
                    } catch (Exception e) {
                        throw new BusinessException(ErrorCode.PAYMENT_CANCEL_FAILED,
                                "결제 취소 오류가 발생했습니다.(ticketId=%d, paymentId=%s)".formatted(ticketId, pr.getPaymentId()));
                    }
                }
                case REQUESTED -> {
                    // 결제 미완료 -> 제거
                    paymentRecordRepository.delete(pr);
                }
                case CANCELLED -> {
                }
            }
        }

        // 좌석/티켓 정리
        reservationSeatRepository.deleteByTicket_Id(ticketId);
        ticketRepository.delete(ticket);

    }

    @Transactional
    public void payForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND, "ticketId="+ticketId));

        String paymentId = paymentIdForTicket(ticket.getId());
        String orderName = "CGV-Ticket-" + ticket.getId();
        String custom = "{\"ticketId\":\"" + ticket.getId() + "\"}";

        // PaymentRecord가 없다면 REQUESTED로 생성, 있다면 그대로 사용(결제 실패 시 다시 사용하기 위함)
        paymentRecordRepository.findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.TICKET, ticketId)
                .or(() -> paymentRecordRepository.findByPaymentId(paymentId))
                .or(() -> {
                    PaymentRecord created = paymentRecordRepository.save(
                            PaymentRecord.createTicket(ticketId, paymentId)
                    );
                    return java.util.Optional.of(created);
                });

        // 실제 결제 호출
        try {
            paymentService.pay(paymentId, orderName, ticket.getFinalPrice(), custom);
            // 성공 -> PaymentRecord를 PAID로 변경
            paymentRecordRepository.findByPaymentId(paymentId)
                    .ifPresent(r -> r.setStatus(PaymentStatus.PAID));

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PAYMENT_FAILED,
                    "결제가 실패했습니다.(ticketId=%d, paymentId=%s)".formatted(ticketId, paymentId));
        }
    }

    /* ========== 유틸 ========== */
    private String seatKey(Long showtimeId, String row, String col) {
        return "seat:%d:%s-%s".formatted(showtimeId, row, col);
    }
    private String paymentIdForTicket(Long ticketId) {
        return "junilyy-tkt-" + ticketId;
    }

    // 최종 금액 계산
    private int calcFinalPrice(TicketRequestDto req) {
        return req.getGeneralCount() * TicketPrice.GENERAL.getPrice()
                + req.getYouthCount()   * TicketPrice.YOUTH.getPrice();
    }

    private String[] parseSeat(String seat) {
        if (seat == null || seat.length() < 2) {
            throw new BusinessException(ErrorCode.BAD_REQUEST_ERROR, "invalid seat=" + seat);
        }
        String row = seat.substring(0, 1);
        String col = seat.substring(1);
        return new String[]{row, col};
    }

    private Showtime getShowtime(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOWTIME_NOT_FOUND, "showtimeId=%d을 찾을 수 없습니다.".formatted(showtimeId)));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "username="+username));
    }
    private Ticket getTicketOwned(String username, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TICKET_NOT_FOUND, "ticketId="+ticketId));
        if (!ticket.getUser().getUsername().equals(username)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_TICKET_ACCESS,
                    "ticketId=%d is not owned by %s".formatted(ticketId, username));
        }
        return ticket;
    }
}