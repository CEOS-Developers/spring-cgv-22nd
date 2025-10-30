package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.payment.PaymentRecordRepository;
import com.ceos22.cgv_clone.domain.shop.repository.OrderItemRepository;
import com.ceos22.cgv_clone.domain.shop.repository.OrderRepository;
import com.ceos22.cgv_clone.domain.shop.repository.ProductRepository;
import com.ceos22.cgv_clone.domain.shop.repository.StockRepository;
import com.ceos22.cgv_clone.domain.theater.repository.TheaterRepository;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.shop.entity.Order;
import com.ceos22.cgv_clone.domain.shop.entity.OrderItem;
import com.ceos22.cgv_clone.domain.shop.entity.Stock;
import com.ceos22.cgv_clone.domain.theater.entity.Theater;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import com.ceos22.cgv_clone.domain.shop.dto.request.OrderItemRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderItemResponseDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentRecord;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentStatus;
import com.ceos22.cgv_clone.domain.payment.entity.PaymentTarget;
import com.ceos22.cgv_clone.domain.payment.service.PaymentService;
import com.ceos22.cgv_clone.global.redis.RedissonLockHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final TheaterRepository theaterRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    private final PaymentService paymentService;
    private final PaymentRecordRepository paymentRecordRepository;

    private final RedissonLockHelper lockHelper;

    private String stockKey(Long theaterId, Long productId) {
        return "snack:%d:%d".formatted(theaterId, productId);
    }

    // 결제용 prefix
    private String paymentIdForOrder(Long orderId) {
        return "junilyy-ord-" + orderId;
    }

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto request) {

        int itemCount = request.getItems() == null ? 0 : request.getItems().size();
        log.debug("[SVC] createOrder start - user={}, theaterId={}, items={}",
                username, request.getTheaterId(), itemCount);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
            Theater theater = theaterRepository.findById(request.getTheaterId())
                    .orElseThrow(() -> new IllegalArgumentException("매점 없음"));

            // 이번 주문의 재고 키들을 수집
            List<String> keys = request.getItems().stream()
                    .map(i -> stockKey(request.getTheaterId(), i.getProductId()))
                    .distinct()
                    .toList();


            Order order = Order.builder()
                    .user(user)
                    .theater(theater)
                    .totalPrice(0)
                    .build();
            ordersRepository.save(order);

            int totalPrice = 0;
            List<OrderItemResponseDto> itemDtos = new ArrayList<>();

            for (OrderItemRequestDto item : request.getItems()) {
                Stock stock = stockRepository
                        .findByProduct_IdAndTheater_Id(item.getProductId(), request.getTheaterId())
                        .orElseThrow(() -> new IllegalArgumentException("재고 없음"));

                // 주문 상품 생성/저장
                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .product(stock.getProduct())
                        .quantity(item.getQuantity())
                        .price(stock.getProduct().getPrice() * item.getQuantity())
                        .build();
                orderItemRepository.save(orderItem);

                totalPrice += orderItem.getPrice();
                itemDtos.add(OrderItemResponseDto.fromEntity(orderItem));
            }

            order.setTotalPrice(totalPrice);

            // 결제 대기 레코드(REQUESTED) 생성
            String paymentId = paymentIdForOrder(order.getId());
            paymentRecordRepository.save(
                    PaymentRecord.builder()
                            .type(PaymentTarget.ORDER)
                            .refId(order.getId())
                            .paymentId(paymentId)
                            .status(PaymentStatus.REQUESTED)
                            .build()
            );

            log.info("[SVC] 주문 생성 완료 - user={}, orderId={}, theaterId={}, items={}, totalPrice={}",
                    username, order.getId(), request.getTheaterId(), itemDtos.size(), totalPrice);

            return OrderResponseDto.fromEntity(order, itemDtos);

        } catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(주문 생성) - user={}, theaterId={}, msg={}",
                    username, request.getTheaterId(), e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.warn("[SVC] 비정상 상태(주문 생성) - user={}, theaterId={}, msg={}",
                    username, request.getTheaterId(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[SVC] 주문 생성 실패 - user={}, theaterId={}", username, request.getTheaterId(), e);
            throw e;
        }
    }

    //주문 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String username, Long orderId) {
        log.debug("[SVC] getOrder start - user={}, orderId={}", username, orderId);
        try {
            Order order = ordersRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

            if (!order.getUser().getUsername().equals(username)) {
                throw new SecurityException("본인의 주문만 조회할 수 있습니다.");
            }

            List<OrderItemResponseDto> items = orderItemRepository.findByOrder_Id(orderId)
                    .stream()
                    .map(OrderItemResponseDto::fromEntity)
                    .toList();

            log.info("[SVC] 주문 조회 완료 - user={}, orderId={}, items={}, totalPrice={}",
                    username, orderId, items.size(), order.getTotalPrice());

            return OrderResponseDto.fromEntity(order, items);

        } catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(주문 조회) - user={}, orderId={}, msg={}", username, orderId, e.getMessage());
            throw e;
        } catch (SecurityException e) {
            log.warn("[SVC] 권한 오류(주문 조회) - user={}, orderId={}, msg={}", username, orderId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("[SVC] 주문 조회 실패 - user={}, orderId={}", username, orderId, e);
            throw e;
        }
    }

    // 주문 결제
    @Transactional
    public void payForOrder(String username, Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new SecurityException("본인 주문만 결제할 수 있습니다.");
        }

        String paymentId = paymentIdForOrder(orderId);
        String orderName = "CGV-Order-" + orderId;
        String custom = "{\"orderId\":\"" + orderId + "\"}";
        int amount = order.getTotalPrice();

        // 결제 호출
        try {
            paymentService.pay(paymentId, orderName, amount, custom);
            paymentRecordRepository.findByPaymentId(paymentId)
                    .ifPresent(r -> r.setStatus(PaymentStatus.PAID));
        } catch (Exception e) {
            log.warn("[SVC] 주문 결제 실패 - orderId={}, msg={}", orderId, e.getMessage());
            throw e;
        }

        // 결제 성공 -> 재고 차감
        var items = orderItemRepository.findByOrder_Id(orderId);
        var theaterId = order.getTheater().getId();

        var keys = items.stream()
                .map(oi -> stockKey(theaterId, oi.getProduct().getId()))
                .distinct().toList();

        try {
            lockHelper.withLocks(keys, 2000, 5000, () -> {
                // 재고 검증
                for (var oi : items) {
                    Stock stock = stockRepository
                            .findByProduct_IdAndTheater_Id(oi.getProduct().getId(), theaterId)
                            .orElseThrow(() -> new IllegalArgumentException("재고 없음"));
                    if (stock.getStock() < oi.getQuantity()) {
                        throw new IllegalStateException("재고 부족: " + oi.getProduct().getId());
                    }
                }
                // 차감
                for (var oi : items) {
                    Stock stock = stockRepository
                            .findByProduct_IdAndTheater_Id(oi.getProduct().getId(), theaterId)
                            .orElseThrow();
                    stock.setStock(stock.getStock() - oi.getQuantity());
                }
                return null;
            });

            log.info("[SVC] 주문 결제/재고 차감 완료 - orderId={}, amount={}", orderId, amount);

        } catch (Exception e) {
            // 재고 차감 실패 시 결제 취소
            try {
                paymentService.cancel(paymentId);
                paymentRecordRepository.findByPaymentId(paymentId)
                        .ifPresent(r -> r.setStatus(PaymentStatus.CANCELLED));
                log.warn("[SVC] 재고 부족으로 인한 결제 취소 완료 - orderId={}, paymentId={}", orderId, paymentId);
            } catch (Exception cancelEx) {
                log.error("[SVC] 결제 보상 취소 실패 - orderId={}, paymentId={}", orderId, paymentId, cancelEx);
            }
            throw e;
        }
    }

    @Transactional
    public void cancelOrder(String username, Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new SecurityException("본인 주문만 취소할 수 있습니다.");
        }

        // 1) 결제 상태 조회
        var recOpt = paymentRecordRepository.findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.ORDER, orderId);

        // 2) 결제 상태별 처리
        if (recOpt.isPresent()) {
            var rec = recOpt.get();

            if (rec.getStatus() == PaymentStatus.PAID) {
                // 결제 완료된 경우 -> CANCELLED로 상태 변경
                try {
                    paymentService.cancel(rec.getPaymentId());
                    rec.setStatus(PaymentStatus.CANCELLED);
                } catch (Exception ex) {
                    throw new RuntimeException("결제 취소 실패", ex);
                }

                // 재고 복구
                restoreStocksAndDeleteOrder(order);

            } else if (rec.getStatus() == PaymentStatus.REQUESTED) {
                // 결제 전이므로 결제 레코드 및 주문 삭제(재고 복구 X)
                paymentRecordRepository.delete(rec);
                deleteOrderOnly(order);
            }
        }

        log.info("[SVC] 주문 취소 완료 - user={}, orderId={}", username, orderId);
    }

    // 재고 복구 후 주문 및 결제 레코드 삭제
    private void restoreStocksAndDeleteOrder(Order order) {
        var items = orderItemRepository.findByOrder_Id(order.getId());
        Long theaterId = order.getTheater().getId();

        var keys = items.stream()
                .map(oi -> "snack:%d:%d".formatted(theaterId, oi.getProduct().getId()))
                .distinct().toList();

        lockHelper.withLocks(keys, 2000, 5000, () -> {
            // 재고 복구
            for (var oi : items) {
                var stock = stockRepository.findByProduct_IdAndTheater_Id(oi.getProduct().getId(), theaterId)
                        .orElseThrow();
                stock.setStock(stock.getStock() + oi.getQuantity());
            }
            orderItemRepository.deleteAll(items);
            ordersRepository.delete(order);
            return null;
        });
    }

    // 재고 복구 없이 주문 삭제
    private void deleteOrderOnly(Order order) {
        var items = orderItemRepository.findByOrder_Id(order.getId());
        orderItemRepository.deleteAll(items);
        ordersRepository.delete(order);
    }
}
