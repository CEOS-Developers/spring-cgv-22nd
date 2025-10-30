package com.ceos22.cgv_clone.domain.store.service;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderDetailDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderRequestDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderResponseDto;
import com.ceos22.cgv_clone.domain.store.dto.ProductOrderSummaryDto;
import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.ProductOrder;
import com.ceos22.cgv_clone.domain.store.entity.ProductOrderItem;
import com.ceos22.cgv_clone.domain.store.repository.InventoryRepository;
import com.ceos22.cgv_clone.domain.store.repository.ProductOrderItemRepository;
import com.ceos22.cgv_clone.domain.store.repository.ProductOrderRepository;
import com.ceos22.cgv_clone.domain.store.repository.ProductRepository;
import com.ceos22.cgv_clone.domain.theater.entity.Cinema;
import com.ceos22.cgv_clone.domain.theater.repository.CinemaRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOrderQueryService {

    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderItemRepository productOrderItemRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final MemberRepository memberRepository;
    private final CinemaRepository cinemaRepository;

    /** 주문 생성 + 응답 반환 */
    @Transactional
    public ProductOrderResponseDto createOrder(String email, ProductOrderRequestDto request) {

        log.debug("주문 생성 시도: {}, cinemaId: {}", email, request.getCinemaId());

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Cinema cinema = cinemaRepository.findById(request.getCinemaId())
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEM_VALIDATION_FAILED);
        }

        // 쿼리 한 번에 조회되도록
        List<Long> productIds = request.getItems().stream()
                .map(ProductOrderRequestDto.OrderItem::getProductId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        // 요청된 상품 ID 중 DB에 없는 것이 있는지 확인
        if (productMap.size() != productIds.size()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 총액 계산 & 응답 아이템
        int totalPrice = 0;
        List<ProductOrderResponseDto.OrderItem> responseItems = new ArrayList<>();

        for (ProductOrderRequestDto.OrderItem item : request.getItems()) {
            Product product = productMap.get(item.getProductId());
            totalPrice += product.getPrice() * item.getQuantity();

            responseItems.add(ProductOrderResponseDto.OrderItem.builder()
                    .productName(product.getName())
                    .quantity(item.getQuantity())
                    .price(product.getPrice())
                    .build());
        }


        // 주문 생성/저장
        ProductOrder order = ProductOrder.create(member, cinema, totalPrice);
        productOrderRepository.save(order);

        // 재고 차감 + 주문 라인 생성/저장
        List<ProductOrderItem> lines = new ArrayList<>();
        for (ProductOrderRequestDto.OrderItem item : request.getItems()) {
            Product product = productMap.get(item.getProductId());

            // 재고 차감
            int updatedRows = inventoryRepository.decrement(cinema.getId(), product.getId(), item.getQuantity());
            if (updatedRows != 1) {

                log.warn("재고 차감 실패. cinemaId: {}, productId: {}, requestedQty: {}",
                        cinema.getId(), product.getId(), item.getQuantity());

                throw new CustomException(ErrorCode.PRODUCT_STOCK_VALIDATION_FAILED);
            }

            lines.add(ProductOrderItem.of(order, product, item.getQuantity()));
        }
        productOrderItemRepository.saveAll(lines);

        log.info("Order 생성. user: {}, orderId: {}, cinemaId: {}",
                email, order.getId(), cinema.getId());

        // 응답 DTO
        return ProductOrderResponseDto.builder()
                .orderId(order.getId())
                .memberId(member.getId())
                .cinemaId(cinema.getId())
                .totalPrice(totalPrice)
                .items(responseItems)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProductOrderSummaryDto> getOrdersByMember(Long memberId) {
        return productOrderRepository.findByMemberIdOrderByIdDesc(memberId)
                .stream()
                .map(o -> new ProductOrderSummaryDto(
                        o.getId(),
                        o.getCinema().getId(),
                        o.getTotalPrice(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductOrderDetailDto getOrder(Long orderId) {
        ProductOrder order = productOrderRepository.findDetailsById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        List<ProductOrderItem> items = productOrderItemRepository.findByProductOrderId(orderId);

        return new ProductOrderDetailDto(
                order.getId(),
                order.getMember().getId(),
                order.getCinema().getId(),
                order.getTotalPrice(),
                items.stream()
                        .map(i -> ProductOrderResponseDto.OrderItem.builder()
                                .productName(i.getProduct().getName())
                                .quantity(i.getOrderQuantity())
                                .price(i.getProduct().getPrice())
                                .build())
                        .toList()
        );
    }
}
