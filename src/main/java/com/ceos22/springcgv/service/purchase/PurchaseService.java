package com.ceos22.springcgv.service.purchase;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.purchase.Purchase;
import com.ceos22.springcgv.domain.purchase.PurchaseDetail;
import com.ceos22.springcgv.domain.snack.Inventory;
import com.ceos22.springcgv.domain.snack.SnackItem;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.dto.payment.PaymentCancelResponseDto;
import com.ceos22.springcgv.dto.payment.PaymentRequestDto;
import com.ceos22.springcgv.dto.payment.PaymentResponseDto;
import com.ceos22.springcgv.dto.purchase.PurchaseItemDto;
import com.ceos22.springcgv.dto.purchase.PurchaseRequestDto;
import com.ceos22.springcgv.external.payment.PaymentClient;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import com.ceos22.springcgv.repository.cinema.CinemaRepository;
import com.ceos22.springcgv.repository.purchase.PurchaseDetailRepository;
import com.ceos22.springcgv.repository.purchase.PurchaseRepository;
import com.ceos22.springcgv.repository.snack.InventoryRepository;
import com.ceos22.springcgv.repository.snack.SnackItemRepository;
import com.ceos22.springcgv.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final UserRepository userRepository;
    private final CinemaRepository cinemaRepository;
    private final SnackItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final PaymentClient paymentClient;

    @Value("${payment.store-id}")
    private String storeId;

    @Transactional
    public Long createPurchase(Long userId, PurchaseRequestDto requestDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findById(requestDto.getCinemaId())
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (PurchaseItemDto itemDto : requestDto.getItems()) {
            SnackItem item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SNACK_NO_STOCK));

            Inventory inventory = inventoryRepository.findByCinemaAndItemForUpdate(cinema, item)
                    .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_FOUND));

            inventory.decreaseQuantity(itemDto.getQuantity());
            totalPrice = totalPrice.add(item.getPrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        // 결제 ID 생성 (날짜 기반)
        String paymentId = generatePaymentId();

        // 결제 요청 생성
        PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
                .storeId(storeId)
                .orderName("매점 결제_" + cinema.getName())
                .totalPayAmount(totalPrice.intValue())
                .currency("KRW")
                .customData("{\"cinema\":\"" + cinema.getName() + "\"}")
                .build();

        PaymentResponseDto paymentResponse = paymentClient.requestPayment(paymentId, paymentRequest);

        // 결제 실패 시 롤백
        if (paymentResponse == null || paymentResponse.getPaymentId() == null) {
            throw new CustomException(ErrorCode.PAYMENT_FAILED);
        }

        // 결제 성공 시 구매 내역 저장
        Purchase purchase = Purchase.builder()
                .user(user)
                .cinema(cinema)
                .paymentId(paymentResponse.getPaymentId())
                .totalPrice(totalPrice)
                .build();
        purchaseRepository.save(purchase);

        for (PurchaseItemDto itemDto : requestDto.getItems()) {
            SnackItem item = itemRepository.findById(itemDto.getItemId()).get();
            PurchaseDetail detail = PurchaseDetail.builder()
                    .purchase(purchase)
                    .item(item)
                    .quantity(itemDto.getQuantity())
                    .build();
            purchaseDetailRepository.save(detail);
        }

        return purchase.getId();
    }

    @Transactional
    public void cancelPurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new CustomException(ErrorCode.PURCHASE_NOT_FOUND));

        // Payment 취소 요청
        String paymentId = purchase.getPaymentId();
        if (paymentId == null) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        PaymentCancelResponseDto cancelResponse = paymentClient.cancelPayment(paymentId);

        // 재고 복구
        for (PurchaseDetail detail : purchaseDetailRepository.findByPurchase(purchase)) {
            Inventory inventory = inventoryRepository.findByCinemaAndItemForUpdate(
                    purchase.getCinema(), detail.getItem()
            ).orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_FOUND));

            inventory.increaseQuantity(detail.getQuantity());
        }

        // 상태 CANCELED로 바꿈
        purchase.cancel();
    }

    private String generatePaymentId() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int) (Math.random() * 9000) + 1000;
        return datePart + "_" + random;
    }
}