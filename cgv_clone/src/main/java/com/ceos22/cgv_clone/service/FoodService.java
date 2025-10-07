package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.orderFood.Food;
import com.ceos22.cgv_clone.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional
    public void saveFood(Food food) {
        foodRepository.save(food);
    }

    public List<Food> findFoods() {
        return foodRepository.findAll();
    }

    public Food findOne(Long id) {
        return foodRepository.findById(id).orElse(null);
    }
}
