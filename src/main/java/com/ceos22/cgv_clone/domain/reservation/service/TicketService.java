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


    // 락 키 유틸
    private String seatKey(Long showtimeId, String row, String col) {
        return "seat:%d:%s-%s".formatted(showtimeId, row, col);
    }

    // ticket prefix
    private String paymentIdForTicket(Long ticketId) {
        return "junilyy-tkt-" + ticketId;
    }

    // 예매
    @Transactional
    public TicketResponseDto reserveTicket(String username, TicketRequestDto request) {
        int seatCount = request.getSeatNumbers() == null ? 0 : request.getSeatNumbers().size();
        log.debug("[SVC] reserveTicket start - user={}, showtime={}, seats={}",
                username, request.getShowtimeId(), seatCount);

        try {
            Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                    .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            // 최종 결제 금액 계산(!!!!!!계산 로직 분리하기!!!!!!!)
            int finalPrice = request.getGeneralCount() * TicketPrice.GENERAL.getPrice()
                    + request.getYouthCount() * TicketPrice.YOUTH.getPrice();

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
                    String row = seatNumber.substring(0, 1);
                    String col = seatNumber.substring(1);

                    boolean alreadyReserved = reservationSeatRepository
                            .existsByShowtime_IdAndSeatRowAndSeatColAndStatus(request.getShowtimeId(), row, col, ReservationStatus.RESERVED);

                    if (alreadyReserved) {
                        throw new IllegalStateException("이미 예약된 좌석입니다.");
                    }

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

                String paymentId = "junilyy-tkt-" + savedTicket.getId();

                // 결제 내역 레코드에 REQUESTED 상태로 저장
                paymentRecordRepository.save(
                        PaymentRecord.builder()
                                .type(PaymentTarget.TICKET)
                                .refId(savedTicket.getId())
                                .paymentId(paymentId)
                                .status(PaymentStatus.REQUESTED)
                                .build()
                );

                List<String> rsSeats = reservedSeats.stream().map(rs -> rs.getSeatRow() + rs.getSeatCol()).toList();

                log.info("[SVC] 예매 완료 - user={}, showtime={}, ticketId={}, seats={}, price={}",
                        username, request.getShowtimeId(), savedTicket.getId(), rsSeats.size(), finalPrice);

                return TicketResponseDto.fromEntity(savedTicket, rsSeats);
            });
        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(예매) - user={}, showtime={}, msg={}",
                    username, request.getShowtimeId(), e.getMessage());
            throw e;
        }
        catch (IllegalStateException e) {
            log.warn("[SVC] 비정상 좌석 요청(중복 등) - user={}, showtime={}, msg={}",
                    username, request.getShowtimeId(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 예매 처리 실패 - user={}, showtime={}", username, request.getShowtimeId(), e);
            throw e;
        }
    }

    @Transactional
    public void cancelTicket(String username, Long ticketId) {
        // 1) 권한 확인
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인이 예매한 티켓만 취소할 수 있습니다.");
        }

        // 2) 최신 결제 레코드 조회
        PaymentRecord pr = paymentRecordRepository
                .findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.TICKET, ticketId)
                .orElse(null);

        String paymentId = "junilyy-tkt-" + ticketId;

        // 3) 상태별 처리
        if (pr != null) {
            switch (pr.getStatus()) {
                case PAID -> {
                    // PG 결제 취소 호출
                    try {
                        paymentService.cancel(paymentId);
                        pr.setStatus(PaymentStatus.CANCELLED);
                        log.info("[SVC] 결제 취소 성공 - ticketId={}, paymentId={}", ticketId, paymentId);
                    } catch (Exception e) {
                        log.warn("[SVC] 결제 취소 실패 - ticketId={}, msg={}", ticketId, e.getMessage());
                        throw e;
                    }
                }
                case CANCELLED -> {
                    // 이미 취소됨 -> 좌석/티켓만 정리
                    log.info("[SVC] 이미 결제 취소된 티켓 - ticketId={}", ticketId);
                }
                case REQUESTED -> {
                    // 결제 미완료 -> 제거
                    paymentRecordRepository.delete(pr);
                    log.info("[SVC] 결제 대기(REQUESTED) 티켓 취소 - ticketId={}, paymentId={}", ticketId, pr.getPaymentId());
                }
            }
        } else {
            log.info("[SVC] 결제 레코드 없음 - ticketId={}", ticketId);
        }

        // 4) 좌석/티켓 정리
        reservationSeatRepository.deleteByTicket_Id(ticketId);
        ticketRepository.delete(ticket);

        log.info("[SVC] 티켓 취소 완료 - user={}, ticketId={}", username, ticketId);
    }

    @Transactional
    public void payForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));

        String paymentId = paymentIdForTicket(ticket.getId());
        String orderName = "CGV-Ticket-" + ticket.getId();
        String custom = "{\"ticketId\":\"" + ticket.getId() + "\"}";

        // PaymentRecord가 없다면 REQUESTED로 생성, 있다면 그대로 사용(결제 실패 시 다시 사용하기 위함)
        paymentRecordRepository.findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.TICKET, ticketId)
                .or(() -> paymentRecordRepository.findByPaymentId(paymentId))
                .or(() -> {
                    PaymentRecord created = paymentRecordRepository.save(
                            PaymentRecord.builder()
                                    .type(PaymentTarget.TICKET)
                                    .refId(ticketId)
                                    .paymentId(paymentId)
                                    .status(PaymentStatus.REQUESTED)
                                    .build()
                    );
                    return java.util.Optional.of(created);
                });

        // 실제 결제 호출
        try {
            paymentService.pay(paymentId, orderName, ticket.getFinalPrice(), custom);

            // 성공 -> PaymentRecord를 PAID로 변경
            paymentRecordRepository.findByPaymentId(paymentId)
                    .ifPresent(r -> r.setStatus(PaymentStatus.PAID));

            log.info("[SVC] 결제 성공 - ticketId={}, paymentId={}", ticketId, paymentId);
        } catch (Exception e) {
            // 실패 시 상태는 그대로 REQUESTED
            // 좌석은 스케줄러가 1분 뒤 반납할 수 있도록 유지
            log.warn("[SVC] 결제 실패 - ticketId={}, paymentId={}, msg={}", ticketId, paymentId, e.getMessage());
            throw e;
        }
    }
}