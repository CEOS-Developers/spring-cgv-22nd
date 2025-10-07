package com.ceos22.spring_boot.domain.order;

import com.ceos22.spring_boot.common.enums.PaymentMethod;
import com.ceos22.spring_boot.common.enums.PaymentStatus;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.order.dto.PurchaseRequestDto;
import com.ceos22.spring_boot.domain.order.dto.PurchaseResponseDto;
import com.ceos22.spring_boot.domain.order.entity.OrderDetail;
import com.ceos22.spring_boot.domain.order.entity.Payment;
import com.ceos22.spring_boot.domain.order.entity.Product;
import com.ceos22.spring_boot.domain.order.entity.UserOrder;
import com.ceos22.spring_boot.domain.order.repository.OrderDetailRepository;
import com.ceos22.spring_boot.domain.order.repository.PaymentRepository;
import com.ceos22.spring_boot.domain.order.repository.ProductRepository;
import com.ceos22.spring_boot.domain.order.repository.UserOrderRepository;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository users;
    private final ProductRepository products;
    private final UserOrderRepository userOrders;
    private final OrderDetailRepository orderDetails;
    private final PaymentRepository payments;

     // 매점 구매(환불 X): 결제 즉시 성공 처리(= RESERVED)
    @Transactional
    public PurchaseResponseDto purchase(Long userId, PurchaseRequestDto req) {
        if (req.items() == null || req.items().isEmpty()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST, "주문 항목이 비어있습니다.");
        }

        User user = users.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED, "인증된 사용자가 아닙니다."));

        // 주문 생성: 일단 PENDING
        UserOrder order = UserOrder.builder()
                .user(user)
                .totalPrice(0)
                .status(PaymentStatus.PENDING)
                .build();
        userOrders.save(order); // 영속화

        int total = 0;

        for (var item : req.items()) {
            Product p = products.findById(item.productId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.PRODUCT_NOT_FOUND, "상품이 존재하지 않습니다. id=" + item.productId()));

            if (!p.isAvailable()) {
                throw new GeneralException(ErrorStatus.PRODUCT_NOT_AVAILABLE, "판매 중지된 상품입니다. id=" + p.getProductId());
            }
            if (p.getStock() < item.quantity()) {
                throw new GeneralException(ErrorStatus.OUT_OF_STOCK, "재고 부족: id=" + p.getProductId());
            }

            // 재고 차감
            p.decreaseStock(item.quantity());

            int unitPrice = p.getPrice();
            int subtotal = unitPrice * item.quantity();
            total += subtotal;

            OrderDetail od = new OrderDetail(
                    null,         // odId
                    order,        // userOrder
                    p,            // product
                    item.quantity(),
                    unitPrice,
                    subtotal
            );
            orderDetails.save(od);
        }

        // 결제 성공
        Payment pay = Payment.builder()
                .reservation(null)
                .userOrder(order)
                .method(req.method() == null ? PaymentMethod.카드 : req.method())
                .status(PaymentStatus.SUCCESS)
                .build();
        payments.save(pay);

        // 주문 총액/상태 업데이트 (더티체킹)
        order.markReserved(total);

        return new PurchaseResponseDto(order.getOrderId(), order.getTotalPrice(), order.getStatus().name());
    }
}
