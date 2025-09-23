package com.ceos22.cgv_clone.domain.region.service;

import com.ceos22.cgv_clone.domain.region.dto.RegionResponse;
import com.ceos22.cgv_clone.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionResponse> findAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponse::from)
                .collect(Collectors.toList());
    }
}
