package com.ceos22.cgv_clone.domain.payment.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import com.ceos22.cgv_clone.domain.order.repository.OrderRepository;
import com.ceos22.cgv_clone.domain.order.service.OrderService;
import com.ceos22.cgv_clone.domain.payment.client.PaymentClient;
import com.ceos22.cgv_clone.domain.payment.dto.request.InstantPaymentRequest;
import com.ceos22.cgv_clone.domain.payment.dto.request.PaymentCreateRequest;
import com.ceos22.cgv_clone.domain.payment.dto.response.InstantPaymentResponse;
import com.ceos22.cgv_clone.domain.payment.dto.response.PaymentResponse;
import com.ceos22.cgv_clone.domain.payment.entity.Payment;
import com.ceos22.cgv_clone.domain.payment.repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @Value("${payment.mock.store-id}")
    private String mockStoreId;

    @Value("${payment.mock.currency:KRW}")
    private String currency;

    @Transactional
    public PaymentResponse createPayment(Member member, PaymentCreateRequest request) {

        Order order = findOrderById(request.orderId());

        validateOrderOwner(order, member);
        validateOrderPending(order);
        validateDuplicatePayment(order);

        String paymentId = generatePaymentId(mockStoreId);
        String customData = createCustomData(order);

        Payment payment = Payment.of(
                order,
                paymentId,
                mockStoreId,
                createOrderName(order),
                order.getTotalPrice(),
                currency,
                customData
        );
        paymentRepository.save(payment);

        try {
            InstantPaymentRequest instantRequest = new InstantPaymentRequest(
                    mockStoreId,
                    payment.getOrderName(),
                    payment.getTotalPayAmount(),
                    payment.getCurrency(),
                    payment.getCustomData()
            );

            InstantPaymentResponse response = paymentClient.instantPayment(paymentId, instantRequest);

            payment.complete();
            orderService.confirmOrderAfterPayment(order.getOrderId());

            log.info("결제 승인 완료 - paymentId: {}, orderId: {}", response.paymentId(), order.getOrderId());

        } catch (Exception e) {
            payment.fail(e.getMessage());
            paymentRepository.save(payment);
            log.error("결제 실패 - paymentId: {}, error: {}", paymentId, e.getMessage());
            throw new CustomException(ErrorCode.PAYMENT_FAILED);
        }

        return PaymentResponse.from(payment);
    }

    @Transactional
    public PaymentResponse cancelPayment(Member member, String paymentId) {

        Payment payment = findPaymentByPaymentId(paymentId);
        Order order = payment.getOrder();

        validateOrderOwner(order, member);

        try {
            paymentClient.cancelPayment(paymentId);
            log.info("Mock 서버 결제 취소 성공 - paymentId: {}", paymentId);
        } catch (Exception e) {
            log.error("Mock 서버 결제 취소 실패 - paymentId: {}, error: {}", paymentId, e.getMessage());
        }

        payment.cancel("사용자 요청");
        orderService.cancelOrderAfterPaymentCancel(order.getOrderId());

        log.info("결제 및 주문 취소 완료 - paymentId: {}, orderId: {}", paymentId, order.getOrderId());

        return PaymentResponse.from(payment);
    }


    public PaymentResponse getPayment(Member member, String paymentId) {
        Payment payment = findPaymentByPaymentId(paymentId);
        validateOrderOwner(payment.getOrder(), member);
        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPaymentByOrder(Member member, Long orderId) {
        Order order = findOrderById(orderId);
        validateOrderOwner(order, member);
        Payment payment = findPaymentByOrder(order);
        return PaymentResponse.from(payment);
    }

    //=================================================================================================================

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Payment findPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    private Payment findPaymentByOrder(Order order) {
        return paymentRepository.findByOrder(order)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
    }

    //=================================================================================================================

    private void validateOrderOwner(Order order, Member member) {
        if (!order.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }
    }

    private void validateDuplicatePayment(Order order) {
        if (paymentRepository.existsByOrder(order)) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_EXISTS);
        }
    }

    private void validateOrderPending(Order order) {
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("결제 요청 실패: 주문 상태가 PENDING이 아님. orderId={}, status={}", order.getOrderId(), order.getStatus());
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }

    //=================================================================================================================

    // 임시로 해두었습니다... 나중에 다른 방식으로 해결할게요ㅜ
    private String generatePaymentId(String storeId) {
        String dateTimePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        long countToday = paymentRepository.countByStoreIdAndPaymentIdStartingWith(
                storeId,
                dateTimePrefix.substring(0, 8)
        );
        return String.format("%s_%04d", dateTimePrefix, countToday + 1);
    }

    private String createOrderName(Order order) {
        int itemCount = order.getOrderDetails().size();
        if (itemCount == 0) {
            return "빈 주문";
        }
        String firstItemName = order.getOrderDetails().get(0).getProduct().getName();

        if (itemCount == 1) {
            return firstItemName;
        }
        return String.format("%s 외 %d건", firstItemName, itemCount - 1);
    }

    private String createCustomData(Order order) {
        try {
            return objectMapper.writeValueAsString(
                    order.getOrderDetails().stream()
                            .map(detail -> new CustomDataItem(
                                    detail.getProduct().getName(),
                                    detail.getQuantity()
                            ))
                            .toList()
            );
        } catch (JsonProcessingException e) {
            log.warn("customData 생성 실패, null 반환 - orderId: {}", order.getOrderId(), e);
            return null;
        }
    }

    private record CustomDataItem(String item, Integer quantity) {}
}