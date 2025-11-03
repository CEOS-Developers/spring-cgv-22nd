package com.ceos22.cgv_clone.domains.orderFood.service;

import com.ceos22.cgv_clone.api.dto.PaymentResponse;
import com.ceos22.cgv_clone.domains.orderFood.adaptor.CinemaReader;
import com.ceos22.cgv_clone.domains.orderFood.adaptor.FoodReader;
import com.ceos22.cgv_clone.domains.orderFood.domain.OrderStatus;
import com.ceos22.cgv_clone.domains.orderFood.dto.InstantPaymentRequest;
import com.ceos22.cgv_clone.domains.orderFood.domain.FoodEntity;
import com.ceos22.cgv_clone.domains.orderFood.domain.OrderEntity;
import com.ceos22.cgv_clone.domains.orderFood.domain.OrderFoodEntity;
import com.ceos22.cgv_clone.domains.orderFood.repository.OrderRepository;
import com.ceos22.cgv_clone.domains.reservationMovie.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodReader foodReader;
    private final CinemaReader cinemaReader;
    private final PaymentService paymentService;
    private final RedissonClient redissonClient;

    /** 주문 */
    @Transactional
    public Long order(Long memberId, Long foodId, Long cinemaId, int count, String currency){
        RLock lock = redissonClient.getLock("Lock:" + cinemaId + ":" + foodId);
        boolean locked = false;

        try {
            locked = lock.tryLock(2, 10, TimeUnit.SECONDS);
            if (!locked) {
                throw new IllegalStateException("다른 주문이 처리 중입니다. 잠시 후 다시 시도해주세요.");
            }

            // 1) 엔티티 조회
            FoodEntity foodEntity = foodReader.findById(foodId);

            // 2) 재고 체크 & 주문 상품 생성
            OrderFoodEntity orderFoodEntity = OrderFoodEntity.createOrderFood(foodEntity, foodEntity.getPrice(), count);

            // 주문 생성
            OrderEntity orderEntity = OrderEntity.createPaymentOrder(memberId, cinemaId, orderFoodEntity);

            // 4) 결제ID 생성 (상점 내 고유) — 예: 날짜+랜덤, 결제 명 생성 - 예: 상품명 외 n건
            String paymentId = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
            String orderName = foodEntity.getName() + " 외 " + (count - 1) + "건";

            // 5) 결제 요청 바디
            InstantPaymentRequest request = new InstantPaymentRequest (
                    cinemaReader.findCinemaName(cinemaId) + "store",
                    orderName,
                    orderEntity.getTotalPrice(),
                    currency, // "KRW"
                    "{\"item\":" + foodId + ",\"count\":" + count + "}"
                    );

            // 6) 외부 결제 호출
            try {
                PaymentResponse payRes = paymentService.pay(paymentId, request);

                // 7) 결제 성공 → 주문 확정 & 결제 정보 세팅
                orderEntity.confirmPayment(paymentId, orderName, request.currency());

                // 8) 주문 저장
                orderRepository.save(orderEntity);

                return orderEntity.getId();
            } catch (WebClientResponseException ex) {
                // 10% 확률 500 발생 → 롤백 처리
                orderEntity.cancel(); // CANCELLED + 재고 원복

                throw new IllegalStateException("결제 실패: " + ex.getRawStatusCode(), ex);
            } catch (RuntimeException ex) {
                // 기타 예외도 동일 처리
                orderEntity.cancel();
                throw ex;
            }


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 대기 중 인터럽트", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    /** 주문 취소**/
    @Transactional
    public void cancel(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
        if (order.getOrderStatus() == OrderStatus.ORDER) {
            try {
                paymentService.cancel(order.getPaymentId());
            } catch (Exception ignore) {
                throw new IllegalArgumentException("결제 취소 도중 오류가 발생하였습니다. 나중에 다시 시도해 주세요.");
            }
        }
        order.cancel();
    }

}
