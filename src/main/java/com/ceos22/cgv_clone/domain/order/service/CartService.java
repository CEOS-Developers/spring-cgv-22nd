package com.ceos22.cgv_clone.domain.order.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.order.dto.request.CartItemRequest;
import com.ceos22.cgv_clone.domain.order.dto.response.CartResponse;
import com.ceos22.cgv_clone.domain.order.entity.Order;
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

import java.util.Collections;
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
        Member member = findMemberById(memberId);
        Store store = findStoreById(storeId);

        Order cart = findCartByMemberAndStore(member, store);

        if (cart == null) {
            return new CartResponse(null, storeId, store.getStoreType(), 0, Collections.emptyList());
        }
        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse addToCart(Long memberId, CartItemRequest request) {
        Member member = findMemberById(memberId);
        Store store = findStoreById(request.storeId());
        Product product = findProductById(request.productId());

        validateStock(store, product, request.quantity());

        Order cart = findOrCreateCart(member, store);
        cart.addToCart(product, request.quantity());

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse updateCartItem(Long memberId, Long storeId, Long productId, Integer newQuantity) {
        Member member = findMemberById(memberId);
        Store store = findStoreById(storeId);
        Product product = findProductById(productId);

        validateStock(store, product, newQuantity);

        Order cart = getExistingCart(member, store);
        cart.updateCartItemQuantity(productId, newQuantity);

        return CartResponse.from(cart);
    }

    @Transactional
    public CartResponse removeFromCart(Long memberId, Long storeId, Long productId) {
        Member member = findMemberById(memberId);
        Store store = findStoreById(storeId);

        Order cart = getExistingCart(member, store);
        cart.removeFromCart(productId);

        return CartResponse.from(cart);
    }

    @Transactional
    public void clearCart(Long memberId, Long storeId) {
        Member member = findMemberById(memberId);
        Store store = findStoreById(storeId);

        Order cart = getExistingCart(member, store);
        cart.clearCart();
    }


    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Order findCartByMemberAndStore(Member member, Store store) {
        List<Order> carts = orderRepository.findByMemberAndStatus(member, OrderStatus.CART)
                .orElse(Collections.emptyList());

        return carts.stream()
                .filter(order -> order.getStore().equals(store))
                .findFirst()
                .orElse(null);
    }

    private Order findOrCreateCart(Member member, Store store) {
        Order cart = findCartByMemberAndStore(member, store);

        if (cart == null) {
            cart = Order.of(member, store);
            return orderRepository.save(cart);
        }
        return cart;
    }

    private Order getExistingCart(Member member, Store store) {
        Order cart = findCartByMemberAndStore(member, store);
        if (cart == null) {
            throw new CustomException(ErrorCode.CART_NOT_FOUND);
        }
        return cart;
    }

    private void validateStock(Store store, Product product, int quantity) {
        if (!storeStockRepository.existsOrderableStock(store, product, quantity)) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }
}