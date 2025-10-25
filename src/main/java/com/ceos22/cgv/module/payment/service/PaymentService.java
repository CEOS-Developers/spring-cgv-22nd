package com.ceos22.cgv.module.payment.service;

import com.ceos22.cgv.module.payment.domain.PaymentLog;
import com.ceos22.cgv.module.payment.dto.DomainAndParams;
import com.ceos22.cgv.module.payment.dto.PaymentApiRequest;
import com.ceos22.cgv.module.payment.dto.PaymentRequest;
import com.ceos22.cgv.module.payment.dto.PaymentResponse;
import com.ceos22.cgv.module.payment.repository.PaymentRepository;
import com.ceos22.cgv.module.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentApiService paymentApiService;
    private final PaymentRepository paymentRepository;
    private final PaymentTransactionService paymentTransactionService;

    @Value("${payment.store-id}")
    private String STORE_ID;
    private final String STORE_PREFIX = "CEOS-22-";

    // 외부 결제 후 내부 트랜잭션으로 확정/차감
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request, User user) {

        boolean isReservation = request.reservationId() != null;
        DomainAndParams params = paymentTransactionService.buildParams(request, isReservation, user);
        String paymentId = paymentTransactionService.createPaymentId();

        PaymentApiRequest paymentApiRequest = new PaymentApiRequest(
                STORE_PREFIX + STORE_ID,
                params.orderName(),
                params.totalPayAmount(),
                params.currency(),
                params.customData()
        );

        // 외부 결제 요청
        PaymentResponse paymentResponse = paymentApiService.createPaymentAPI(paymentId, paymentApiRequest);

        // 예매 결제 확정
        if(isReservation){
            paymentTransactionService.confirmReservation(
                    params.reservation(),
                    paymentId,
                    params,
                    paymentResponse.paidAt()
            );
        }

        // 매점 결제 확정 및 재고 차감
        // 재고 부족 및 lock 획득 실패에 대한 예외처리
        if(!isReservation){
            try{
                paymentTransactionService.confirmOrder(
                        params.order(),
                        paymentId,
                        params,
                        paymentResponse.paidAt()
                );
            } catch (IllegalStateException ex){
                // 재고 부족
                paymentApiService.cancelPaymentAPI(paymentId); // 외부 결제 취소
                paymentTransactionService.saveCancelledPaymntLog( // 결제 취소 내역 저장
                        paymentId,
                        params,
                        paymentResponse.paidAt()
                );
                paymentTransactionService.cancelOrder(params.order()); // 주문 cancel 상태 반영

                throw new ResponseStatusException(HttpStatus.CONFLICT, "재고 부족으로 인한 결제 취소");
            } catch(PessimisticLockException | LockTimeoutException e){
                // 락 타임아웃/경합
                paymentApiService.cancelPaymentAPI(paymentId); // 외부 결제 취소
                paymentTransactionService.saveCancelledPaymntLog( // 결제 취소 내역 저장
                        paymentId,
                        params,
                        paymentResponse.paidAt()
                );
                paymentTransactionService.cancelOrder(params.order()); // 주문 cancel 상태 반영
                throw new ResponseStatusException(HttpStatus.LOCKED, "재고 잠금 경합/타임아웃");
            }
        }
        return paymentResponse;
    }


    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentLogId) {
        String paymentId = paymentRepository.findById(paymentLogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 결제 로그가 존재하지 않습니다."))
                .getPaymentId();

        return paymentApiService.getPaymentAPI(paymentId);
    }

    @Transactional
    public PaymentResponse cancelPayment(Long paymentLogId) {

        PaymentLog paymentLog = paymentRepository.findById(paymentLogId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 결제 로그가 존재하지 않습니다."));

        // 예약 상태 취소 처리
        if (paymentLog.getReservation() != null) {
            paymentTransactionService.cancelReservation(paymentLog.getReservation());
        }

        // 매점 주문 취소 처리
        if (paymentLog.getOrder() != null) {

            int maxRetries = 3;
            long backoffMs = 200L;
            boolean restored = false;
            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    paymentTransactionService.rollbackMenuStock(paymentLog.getOrder());
                    restored = true;
                    break;
                } catch (PessimisticLockException | LockTimeoutException e) {
                    if (attempt == maxRetries) {
                        throw new ResponseStatusException(HttpStatus.LOCKED, "재고 잠금 경합/타임아웃으로 인한 결제 취소 실패(" + attempt + "회 시도)");
                    }
                    try {
                        Thread.sleep(backoffMs * attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            if (restored) {
                paymentTransactionService.cancelOrder(paymentLog.getOrder());
            }
        }

        // 외부 결제 취소 요청
        PaymentResponse paymentResponse = paymentApiService.cancelPaymentAPI(paymentLog.getPaymentId());

        // 내부 결제 내역 취소 처리
        paymentTransactionService.cancelPaymentLog(paymentLog);

        return paymentResponse;
    }

}
