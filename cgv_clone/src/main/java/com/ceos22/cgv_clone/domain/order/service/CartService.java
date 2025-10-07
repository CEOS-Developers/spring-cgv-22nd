package com.ceos22.cgv_clone.domain.order.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.order.dto.request.CartItemRequest;
import com.ceos22.cgv_clone.domain.order.dto.response.CartResponse;
import com.ceos22.cgv_clone.domain.order.entity.Order;
import com.ceos22.cgv_clone.domain.order.entity.OrderDetail;
import com.ceos22.cgv_clone.domain.order.entity.OrderStatus;
import com.ceos22.cgv_clone.domain.order.repository.OrderRepository;
import com.ceos22.cgv_clone.domain.store.entity.Product;
import com.ceos22.cgv_clone.domain.store.entity.Store;
import com.ceos22.cgv_clone.domain.store.repository.ProductRepository;
import com.ceos22.cgv_clone.domain.store.repository.StoreRepository;
import com.ceos22.cgv_clone.domain.store.repository.StoreStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreStockRepository storeStockRepository;

    public CartResponse getCart(Long memberId, Long storeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        List<Order> carts = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElse(List.of());

        Order cart = carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElse(null);

        if (cart == null) {
            return new CartResponse(null, storeId, store.getStoreType(), 0, List.of());
        }
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse addToCart(Long memberId, CartItemRequest request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!storeStockRepository.existsOrderableStock(store, product, request.quantity())) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        List<Order> carts = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElse(List.of());

        Order cart = carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElseGet(() -> {
                    Order newCart = Order.of(member, store);
                    return orderRepository.save(newCart);
                });

        OrderDetail orderDetail = OrderDetail.of(product, request.quantity());
        cart.addToCart(orderDetail);

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long memberId, Long storeId, Long productId, Integer newQuantity) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Order> carts = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Order cart = carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        if (!storeStockRepository.existsOrderableStock(store, product, newQuantity)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }

        cart.updateCartItemQuantity(productId, newQuantity);

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeFromCart(Long memberId, Long storeId, Long productId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 장바구니 조회
        List<Order> carts = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Order cart = carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        cart.removeFromCart(productId);

        return CartResponse.from(cart);
    }

    @Transactional
    public void clearCart(Long memberId, Long storeId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        List<Order> carts = orderRepository.findByMemberAndOrderStatus(member, OrderStatus.PENDING)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        Order cart = carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        cart.clearCart();
    }
}