package com.ceos22.cgv_clone.service.food;

import com.ceos22.cgv_clone.domain.member.MemberEntity;
import com.ceos22.cgv_clone.domain.orderFood.FoodEntity;
import com.ceos22.cgv_clone.domain.orderFood.OrderEntity;
import com.ceos22.cgv_clone.domain.orderFood.OrderFoodEntity;
import com.ceos22.cgv_clone.domain.reservationMovie.CinemaEntity;
import com.ceos22.cgv_clone.repository.CinemaRepository;
import com.ceos22.cgv_clone.repository.FoodRepository;
import com.ceos22.cgv_clone.repository.MemberRepository;
import com.ceos22.cgv_clone.repository.OrderRepository;
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
        OrderEntity orderEntity = OrderEntity.createOrder(memberId, cinemaId, orderFoodEntity);

        // 주문 저장
        orderRepository.save(orderEntity);
        return orderEntity.getId();
    }

    /** 주문 취소 (미구현)*/

}
