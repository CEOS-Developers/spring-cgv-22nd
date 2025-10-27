package com.ceos22.cgv_clone.service.food;

import com.ceos22.cgv_clone.domain.orderFood.FoodEntity;
import com.ceos22.cgv_clone.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodReader {
    private final FoodRepository foodRepository;

    public FoodEntity findById(Long id) {
        return foodRepository.findById(id).orElse(null);
    }

}
