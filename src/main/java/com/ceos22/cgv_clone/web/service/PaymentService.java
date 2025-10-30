package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.service.PaymentClient;
import com.ceos22.cgv_clone.global.service.PaymentIdGenerator;
import com.ceos22.cgv_clone.web.domain.Payment;
import com.ceos22.cgv_clone.web.domain.Product;
import com.ceos22.cgv_clone.web.domain.Schedule;
import com.ceos22.cgv_clone.web.domain.enums.PaymentCategory;
import com.ceos22.cgv_clone.web.domain.enums.PaymentStatus;
import com.ceos22.cgv_clone.web.dto.PaymentRequestDto;
import com.ceos22.cgv_clone.web.dto.PaymentResponseDto;
import com.ceos22.cgv_clone.web.dto.PurchaseRequestDto;
import com.ceos22.cgv_clone.web.dto.ReservationRequestDto;
import com.ceos22.cgv_clone.web.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    @Value("${payment.store-id}")
    private String storeId;

    @Value("${payment.currency}")
    private String currency;

    @Value("${payment.pgProvider}")
    private String pgProvider;

    private final PaymentClient paymentClient;
    private final PaymentIdGenerator paymentIdGenerator;
    private final PaymentRepository paymentRepository;

    public Payment processPurchasePayment(List<Product> products,
                                          List<PurchaseRequestDto.ProductDto> items) {
        String paymentUuid = paymentIdGenerator.generate();
        String orderName = purchasePaymentName(products, items);
        String customData = purchaseCustomData(items);
        int totalPayAmount = calculatePurchaseAmount(products, items);

        PaymentRequestDto requestDto = PaymentRequestDto.of(storeId,orderName,totalPayAmount,currency,customData);

        PaymentResponseDto.PaymentDto response = paymentClient.payInstant(paymentUuid, requestDto);

        Payment payment = Payment.create(
                response.getPaymentId(),
                orderName,
                currency,
                customData,
                pgProvider,
                PaymentCategory.PURCHASE
        );
        payment.setPaidAt(LocalDateTime.parse(response.getPaidAt()));
        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }

    public Payment processReservationPayment(Schedule schedule, ReservationRequestDto.ReservationDto requestdto){

        String movieTitle = schedule.getMovie().getTitle();
        int totalSeats = requestdto.getAdultAmount() + requestdto.getTeenAmount();
        String orderName = reservationPaymentName(movieTitle, totalSeats);

        //결제 API 호출
        String paymentUuid = paymentIdGenerator.generate();

        int totalPayAmount = requestdto.getAdultAmount() * 10000 + requestdto.getTeenAmount() * 8000;
        PaymentRequestDto paymentRequestDto = PaymentRequestDto.of(storeId,orderName,totalPayAmount,currency,null);

        PaymentResponseDto.PaymentDto payRes = paymentClient.payInstant(paymentUuid, paymentRequestDto);

        Payment payment = Payment.create(
                payRes.getPaymentId(),
                orderName,
                paymentRequestDto.getCurrency(),
                paymentRequestDto.getCustomData(),
                storeId,
                PaymentCategory.RESERVATION
        );
        payment.setPaidAt(LocalDateTime.parse(payRes.getPaidAt()));
        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }

    public void cancelPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PAYMENT_NOT_FOUND));

        // 이미 취소된 결제인지 확인
        if (payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new GeneralException(ErrorStatus.PAYMENT_ALREADY_CANCELLED);
        }

        try {
            paymentClient.cancelPayment(payment.getPaymentUuid());

            // 결제 상태 업데이트
            payment.setStatus(PaymentStatus.CANCELLED);
            payment.setCancelledAt();
            paymentRepository.save(payment);

        } catch (Exception e) {
            log.error("Payment cancellation failed for paymentId: {}", paymentId, e);
            throw new GeneralException(ErrorStatus.PAYMENT_CANCEL_FAILED);
        }
    }

    private String purchasePaymentName(List<Product> products, List<PurchaseRequestDto.ProductDto> items) {
        //첫 상품명 + "외 n개"
        String firstName = products.get(0).getName();
        int otherCount = items.size() - 1;
        return otherCount > 0 ? firstName + " 외 " + otherCount + "개" : firstName;
    }

    private String reservationPaymentName(String movieTitle, int totalSeats) {
        return movieTitle + " (" + totalSeats + "매)";
    }

    private String purchaseCustomData(List<PurchaseRequestDto.ProductDto> items) {
        return items.stream()
                .map(dto -> dto.getProductId() + ":" + dto.getQuantity())
                .collect(Collectors.joining(","));
    }

    private int calculatePurchaseAmount(
            List<Product> products,
            List<PurchaseRequestDto.ProductDto> items) {

        return items.stream()
                .mapToInt(item -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(item.getProductId()))
                            .findFirst()
                            .orElseThrow(() -> new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND));
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }
}
