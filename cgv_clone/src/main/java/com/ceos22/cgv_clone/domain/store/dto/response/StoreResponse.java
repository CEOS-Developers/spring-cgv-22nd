package com.ceos22.cgv_clone.domain.store.dto.response;

import com.ceos22.cgv_clone.domain.store.entity.Store;

public record StoreResponse(
        Long storeId,
        String storeTypeDisplayName,
        String theaterName,
        Long theaterId
) {
    public static StoreResponse from(Store store) {
        return new StoreResponse(
                store.getStoreId(),
                store.getStoreType().getDisplayName(),
                store.getTheater().getName(),
                store.getTheater().getTheaterId()
        );
    }
}