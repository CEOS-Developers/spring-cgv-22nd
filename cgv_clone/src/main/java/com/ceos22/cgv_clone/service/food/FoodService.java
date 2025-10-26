package com.ceos22.cgv_clone.service.food;

import com.ceos22.cgv_clone.domain.orderFood.FoodEntity;
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
    private final FoodReader foodReader;

    @Transactional
    public void saveFood(FoodEntity foodEntity) {
        foodRepository.save(foodEntity);
    }

    public List<FoodEntity> findFoods() {
        return foodRepository.findAll();
    }

    public FoodEntity findOne(Long id) {
        return foodRepository.findById(id).orElse(null);
    }
}
