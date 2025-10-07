package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.orderFood.Food;
import com.ceos22.cgv_clone.domain.orderFood.Order;
import com.ceos22.cgv_clone.domain.orderFood.OrderFood;
import com.ceos22.cgv_clone.domain.reservationMovie.Cinema;
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
        Member member = memberRepository.findById(memberId).orElse(null);
        Food food = foodRepository.findById(foodId).orElse(null);
        Cinema cinema = cinemaRepository.findById(cinemaId).orElse(null);

        // 주문 상품 생성
        OrderFood orderFood = OrderFood.createOrderFood(food, food.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, cinema, orderFood);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /** 주문 취소 (미구현)*/

}
