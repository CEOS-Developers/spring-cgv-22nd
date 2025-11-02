package com.ceos22.cgv_clone.domains.orderFood.adaptor;

import com.ceos22.cgv_clone.domains.orderFood.domain.FoodEntity;
import com.ceos22.cgv_clone.domains.orderFood.repository.FoodRepository;
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
