package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.domain.payment.PaymentRecordRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.TicketRepository;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentRecord;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentStatus;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestExpiryJob {

    private final PaymentRecordRepository paymentRecordRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final TicketRepository ticketRepository;

    // 10초마다 스캔
    @Scheduled(fixedDelay = 10_000)
    @Transactional
    public void expireOldRequestedPayments() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(1);

        // 1분 지난 REQUESTED 결제 건 수집 (티켓팅 대상만)
        List<PaymentRecord> stale = paymentRecordRepository
                .findByTypeAndStatusAndCreatedAtBefore(PaymentTarget.TICKET, PaymentStatus.REQUESTED, cutoff);

        int released = 0;
        for (PaymentRecord pr : stale) {
            Long ticketId = pr.getRefId();

            PaymentRecord latest = paymentRecordRepository
                    .findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.TICKET, ticketId)
                    .orElse(null);

            if (latest == null || latest.getStatus() != PaymentStatus.REQUESTED) {
                // 이미 PAID/CANCELLED/삭제 등 → 스킵
                continue;
            }

            // 좌석 해제 -> 티켓 해제
            reservationSeatRepository.deleteByTicket_Id(ticketId);
            ticketRepository.deleteById(ticketId);
            // PaymentRecord 삭제
            paymentRecordRepository.delete(latest);

            released++;
        }

        if (released > 0) {
            log.info("[JOB] 결제대기 만료 처리 완료 - 좌석 반납: {}건", released);
        }
    }
}
