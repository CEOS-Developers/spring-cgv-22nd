package com.ceos22.cgv_clone.domain.shop.service;

import com.ceos22.cgv_clone.domain.payment.PaymentRecordRepository;
import com.ceos22.cgv_clone.domain.shop.repository.OrderItemRepository;
import com.ceos22.cgv_clone.domain.shop.repository.OrderRepository;
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
import com.ceos22.cgv_clone.global.code.ErrorCode;
import com.ceos22.cgv_clone.global.exception.BusinessException;
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

    private final PaymentService paymentService;
    private final PaymentRecordRepository paymentRecordRepository;

    private final RedissonLockHelper lockHelper;


    /* ------------util------------- */
    // lock
    private String stockKey(Long theaterId, Long productId) {
        return "snack:%d:%d".formatted(theaterId, productId);
    }

    // 결제용 prefix
    private String paymentIdForOrder(Long orderId) {
        return "junilyy-ord-" + orderId;
    }


    /* ----------------서비스 로직(주문 생성, 조회, 결제, 결제 취소 등)-----------------*/

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(String username, OrderRequestDto request) {

        User user = getUser(username);
        Theater theater = getTheater(request.getTheaterId());

        // 이번 주문의 재고 키들을 수집
        List<String> keys = request.getItems().stream()
                .map(i -> stockKey(request.getTheaterId(), i.getProductId()))
                .distinct()
                .toList();

        // 주문 저장
        Order order = Order.create(user, theater);
        ordersRepository.save(order);

        int totalPrice = 0;
        List<OrderItemResponseDto> itemDtos = new ArrayList<>();

        for (OrderItemRequestDto item : request.getItems()) {
            Stock stock = getStock(item.getProductId(), request.getTheaterId());

            // 주문 상품 생성/저장
            OrderItem orderItem = OrderItem.create(order, stock.getProduct(), item.getQuantity());
            orderItemRepository.save(orderItem);

            // 총 가격 계산
            totalPrice += orderItem.getPrice();
            itemDtos.add(OrderItemResponseDto.fromEntity(orderItem));
        }

        order.setTotalPrice(totalPrice);

        // 결제 대기 레코드(REQUESTED) 생성
        String paymentId = paymentIdForOrder(order.getId());
        paymentRecordRepository.save(PaymentRecord.createOrder(order.getId(), paymentId));

        return OrderResponseDto.fromEntity(order, itemDtos);
    }

    //주문 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(String username, Long orderId) {

        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        if (!order.getUser().getUsername().equals(username)) {
            throw new SecurityException("본인의 주문만 조회할 수 있습니다.");
        }

        List<OrderItemResponseDto> items = orderItemRepository.findByOrder_Id(orderId)
                .stream()
                .map(OrderItemResponseDto::fromEntity)
                .toList();

        return OrderResponseDto.fromEntity(order, items);

    }

    // 주문 결제
    @Transactional
    public void payForOrder(String username, Long orderId) {
        Order order = getOrderOwned(username, orderId);

        String paymentId = paymentIdForOrder(orderId);
        PaymentRecord record = paymentRecordRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.PAYMENT_REQUESTED,
                        "paymentId=%s를 찾을 수 없습니다.".formatted(paymentId)
                ));

        // 중복 결제 방지
        if (record.getStatus() == PaymentStatus.PAID) {
            throw new BusinessException(
                    ErrorCode.PAYMENT_ALREADY_PAID,
                    "orderId=%d는 이미 결제가 완료되었습니다.".formatted(orderId));
        }

        // 결제 호출
        try {
            String orderName = "CGV-Order-" + orderId;
            String custom = "{\"orderId\":\"" + orderId + "\"}";
            int amount = order.getTotalPrice();
            paymentService.pay(paymentId, orderName, amount, custom);
            record.setStatus(PaymentStatus.PAID);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.PAYMENT_FAILED,
                    "orderId=%d의 결제 오류가 발생하였습니다.".formatted(orderId));
        }

        // 결제 성공 -> 재고 차감
        var items = orderItemRepository.findByOrder_Id(orderId);
        var theaterId = order.getTheater().getId();

        var keys = items.stream()
                .map(oi -> stockKey(theaterId, oi.getProduct().getId()))
                .distinct().toList();

        lockHelper.withLocks(keys, 2000, 5000, () -> {
            // 재고 검증
            for (var oi : items) {
                Stock stock = getStock(oi.getProduct().getId(), theaterId);
                if (stock.getStock() < oi.getQuantity()) {
                    throw new BusinessException(
                            ErrorCode.STOCK_SHORTAGE,
                            "재고가 부족합니다.(productId=%d, need=%d, has=%d)".formatted(oi.getProduct().getId(), oi.getQuantity(), stock.getStock())
                    );
                }
            }
            // 차감
            for (var oi : items) {
                Stock stock = getStock(oi.getProduct().getId(), theaterId);
                stock.setStock(stock.getStock() - oi.getQuantity());
            }
            return null;
        });

    }

    @Transactional
    public void cancelOrder(String username, Long orderId) {
        Order order = getOrderOwned(username, orderId);

        // 결제 상태 조회
        var recOpt = paymentRecordRepository.findTopByTypeAndRefIdOrderByIdDesc(PaymentTarget.ORDER, orderId);

        var rec = recOpt.get();

        if (rec.getStatus() == PaymentStatus.PAID) {
            // 결제 취소
            try {
                paymentService.cancel(rec.getPaymentId());
                rec.setStatus(PaymentStatus.CANCELLED);
            } catch (Exception ex) {
                throw new BusinessException(ErrorCode.PAYMENT_CANCEL_FAILED,
                        "결제 취소 오류가 발생했습니다.(orderId=%d, paymentId=%s)".formatted(orderId, rec.getPaymentId()));
            }
            // 재고 복구 후 주문 삭제
            restoreStocksAndDeleteOrder(order);
        }
        else if (rec.getStatus() == PaymentStatus.REQUESTED) {
            // 레코드 및 주문 삭제
            paymentRecordRepository.delete(rec);
            deleteOrderOnly(order);
        } else {
            deleteOrderOnly(order);
        }

    }

    /* ---------- 내부 공통 로직 --------- */

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.USER_NOT_FOUND, "username=%s을 찾을 수 없습니다.".formatted(username)));
    }

    private Theater getTheater(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.THEATER_NOT_FOUND, "theaterId=%d인 매점을 찾을 수 없습니다.".formatted(theaterId)));
    }

    private Stock getStock(Long productId, Long theaterId) {
        return stockRepository.findByProduct_IdAndTheater_Id(productId, theaterId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.STOCK_NOT_FOUND,
                        "상품을 찾을 수 없습니다.(productId=%d, theaterId=%d)".formatted(productId, theaterId)));
    }

    private Order getOrderOwned(String username, Long orderId) {
        Order order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.ORDER_NOT_FOUND, "orderId=%d인 주문을 찾을 수 없습니다.".formatted(orderId)));
        if (!order.getUser().getUsername().equals(username)) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN_ORDER_ACCESS,
                    "본인의 주문이 아닙니다.(username=%s, owner=%s, orderId=%d)".formatted(username, order.getUser().getUsername(), orderId)
            );
        }
        return order;
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
