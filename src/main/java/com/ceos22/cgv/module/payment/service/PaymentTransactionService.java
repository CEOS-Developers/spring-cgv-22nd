package com.ceos22.cgv.module.payment.service;

import com.ceos22.cgv.common.util.PaymentCategory;
import com.ceos22.cgv.common.util.PaymentStatus;
import com.ceos22.cgv.module.payment.domain.PaymentLog;
import com.ceos22.cgv.module.payment.dto.PaymentApiRequest;
import com.ceos22.cgv.module.payment.dto.PaymentRequest;
import com.ceos22.cgv.module.payment.repository.PaymentRepository;
import com.ceos22.cgv.module.reservation.domain.Reservation;
import com.ceos22.cgv.module.reservation.repository.ReservationRepository;
import com.ceos22.cgv.module.snack.domain.Menu;
import com.ceos22.cgv.module.snack.domain.Order;
import com.ceos22.cgv.module.snack.domain.OrderItem;
import com.ceos22.cgv.module.snack.repository.MenuRepository;
import com.ceos22.cgv.module.snack.repository.OrderRepository;
import com.ceos22.cgv.module.user.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    @Value("${payment.secret-key}")
    private String SECRET_KEY;

    @Value("${payment.store-id}")
    private String STORE_ID;

    @Value("${payment.store-prefix}")
    private String STORE_PREFIX;

    @Value("${payment.base-url}")
    private String BASE_URL;

    private final String PG_PROVIDER = "CEOS_PAY";

    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void confirmReservation(Reservation reservation,
                                      String paymentId,
                                      PaymentApiRequest parameters,
                                      LocalDateTime paidAt) {
        PaymentLog paymentLog = PaymentLog.builder()
                .paymentId(paymentId)
                .orderName(parameters.orderName())
                .totalAmount(parameters.totalPayAmount())
                .currency(parameters.currency())
                .pgProvider(PG_PROVIDER)
                .paymentCategory(PaymentCategory.Reservation)
                .paymentStatus(PaymentStatus.PAID)
                .reservation(reservation)
                .order(null)
                .paidAt(paidAt)
                .build();

        paymentRepository.save(paymentLog);
        reservation.confirm();
        reservationRepository.save(reservation);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void confirmOrder(Order order,
                                String paymentId,
                                PaymentApiRequest parameters,
                                LocalDateTime paidAt) {

        // 1) 락 획득을 위한 메뉴 ID 수집
        List<OrderItem> items = order.getItems();
        List<Long> menuIds = items.stream().map(item -> item.getMenu().getMenuId()).distinct().sorted().collect(Collectors.toList());

        // 2) Pessimistic Lock으로 메뉴 로드(SELECT FOR UPDATE)
        List<Menu> lockedMenus = menuRepository.findAllForUpdateOrderByIdAsc(menuIds);
        Map<Long, Menu> menuMap = lockedMenus.stream().collect(Collectors.toMap(Menu::getMenuId, menu -> menu));

        // 3) 재고 검증 및 차감
        for (OrderItem item : items) {
            Menu menu = menuMap.get(item.getMenu().getMenuId());
            menu.decreaseQuantity(item.getQuantity());
        }

        // 4) 주문/로그 확정 저장
        order.pay();
        orderRepository.save(order);
        PaymentLog paymentLog = PaymentLog.builder()
                .paymentId(paymentId)
                .orderName(parameters.orderName())
                .totalAmount(parameters.totalPayAmount())
                .currency(parameters.currency())
                .pgProvider(PG_PROVIDER)
                .paymentCategory(PaymentCategory.SNACK)
                .paymentStatus(PaymentStatus.PAID)
                .reservation(null)
                .order(order)
                .paidAt(paidAt)
                .build();
        paymentRepository.save(paymentLog);
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void saveCancelledPaymentLog(String paymentId, PaymentApiRequest parameters, LocalDateTime paidAt) {

        PaymentLog log = PaymentLog.builder()
                .paymentId(paymentId)
                .orderName(parameters.orderName())
                .totalAmount(parameters.totalPayAmount())
                .currency(parameters.currency())
                .pgProvider(PG_PROVIDER)
                .paymentCategory(PaymentCategory.SNACK)
                .paymentStatus(PaymentStatus.CANCELLED)
                .reservation(parameters.reservation())
                .order(parameters.order())
                .paidAt(paidAt)
                .build();

        paymentRepository.save(log);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelReservation(Reservation r) {

        // 동일 객체에 대한 중복 변경 시도를 방지하기 위해 DB에서 다시 조회
        Reservation reservation = reservationRepository.findByIdWithScheduleAndSeats(r.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."));

        reservation.cancel();
        reservation.getReservationSeats().clear();
        reservationRepository.save(reservation);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelPaymentLog(PaymentLog paymentLog) {
        paymentLog.cancel();
        paymentRepository.save(paymentLog);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cancelOrder(Order order) {
        order.cancel();
        orderRepository.save(order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rollbackMenuStock(Order order) {

        // 1) 락 획득을 위한 메뉴 ID 수집
        List<OrderItem> items = order.getItems();
        List<Long> menuIds = items.stream().map(item -> item.getMenu().getMenuId()).distinct().sorted().collect(Collectors.toList());

        // 2) Pessimistic Lock으로 메뉴 로드(SELECT FOR UPDATE)
        List<Menu> lockedMenus = menuRepository.findAllForUpdateOrderByIdAsc(menuIds);
        Map<Long, Menu> menuMap = lockedMenus.stream().collect(Collectors.toMap(Menu::getMenuId, menu -> menu));

        // 3) 재고 검증 및 차감
        for (OrderItem item : items) {
            Menu menu = menuMap.get(item.getMenu().getMenuId());
            menu.increaseQuantity(item.getQuantity());
        }
    }

    public record CustomData(
            String type,
            Long reservationId,
            Long orderId,
            Long userId) {}

    public String toJson(Object obj) {
        try { return objectMapper.writeValueAsString(obj); }
        catch (JsonProcessingException e) { return String.valueOf(obj); }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentApiRequest buildParams(PaymentRequest request, boolean isReservation, User user) {

        String storeId = STORE_PREFIX + STORE_ID;
        String orderName;
        Integer total;
        String currency = "KRW";
        String customData;

        if (isReservation) {

            Reservation reservation = reservationRepository.findByIdWithScheduleAndSeats(request.reservationId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "예약이 존재하지 않습니다."));
            int seatCount = reservation.getReservationSeats().size();
            Integer price = reservation.getSchedule().getTheater().getPrice();
            total = seatCount * price;
            orderName = "Reservation-" + reservation.getId() + "-User-" + user.getId();
            customData = toJson(new CustomData("reservation", reservation.getId(), null, user.getId()));

            return new PaymentApiRequest(
                    storeId,
                    orderName,
                    total,
                    currency,
                    customData,
                    reservation,
                    null
            );

        } else {

            Order order = orderRepository.findByIdWithItems(request.orderId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."));
            total = order.getTotalPrice();
            orderName = "Order-" + order.getId() + "-User-" + user.getId();
            customData = toJson(new CustomData("order", null, order.getId(), user.getId()));

            return new PaymentApiRequest(
                    storeId,
                    orderName,
                    total,
                    currency,
                    customData,
                    null,
                    order
            );
        }
    }

    public String createPaymentId() {

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS"));

        return STORE_ID + "_" + timestamp;
    }
}
