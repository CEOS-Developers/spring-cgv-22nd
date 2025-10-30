package com.ceos22.cgv_clone.domain.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreType {
    POPCORN_FACTORY("팝콘팩토리"),
    CINE_PUB("씨네펍"),
    SWEET_SHOP("스위트샵");

    private final String displayName;
}
