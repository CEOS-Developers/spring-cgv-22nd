package com.ceos22.cgv_clone.domain.common.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class CountResponse {

    private final long count;

    public static CountResponse of(long c) { return new CountResponse(c); }
}
