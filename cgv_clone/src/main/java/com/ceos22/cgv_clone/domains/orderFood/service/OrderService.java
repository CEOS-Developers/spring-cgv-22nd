package com.ceos22.cgv_clone.domains.orderFood.service;

import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;
import com.ceos22.cgv_clone.domains.orderFood.domain.FoodEntity;
import com.ceos22.cgv_clone.domains.orderFood.domain.OrderEntity;
import com.ceos22.cgv_clone.domains.orderFood.domain.OrderFoodEntity;
import com.ceos22.cgv_clone.domains.reservationMovie.domain.CinemaEntity;
import com.ceos22.cgv_clone.domains.reservationMovie.repository.CinemaRepository;
import com.ceos22.cgv_clone.domains.orderFood.repository.FoodRepository;
import com.ceos22.cgv_clone.domains.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domains.orderFood.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final MemberRepository memberRepository;
    private final CinemaRepository cinemaRepository;

    /** 주문 */
    @Transactional
    public Long order(Long memberId, Long foodId, Long cinemaId, int count){

        // 엔티티 조회
        MemberEntity member = memberRepository.findById(memberId).orElse(null);
        FoodEntity foodEntity = foodRepository.findById(foodId).orElse(null);
        CinemaEntity cinemaEntity = cinemaRepository.findById(cinemaId).orElse(null);

        // 주문 상품 생성
        OrderFoodEntity orderFoodEntity = OrderFoodEntity.createOrderFood(foodEntity, foodEntity.getPrice(), count);

        // 주문 생성
        OrderEntity orderEntity = OrderEntity.createPaymentOrder(memberId, cinemaId, orderFoodEntity);

        // 결제

        // 주문 저장
        orderRepository.save(orderEntity);
        return orderEntity.getId();
    }

    /** 주문 취소 (미구현)*/

}
