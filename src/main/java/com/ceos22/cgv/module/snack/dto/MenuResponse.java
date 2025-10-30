package com.ceos22.cgv.module.snack.dto;

import com.ceos22.cgv.module.snack.domain.Menu;

public record MenuResponse(
        Long id,
        String name,
        Integer price,
        String shortDescription,
        String description
) {
    public static MenuResponse fromMenu(Menu menu) {
        return new MenuResponse(
                menu.getMenuId(),
                menu.getName(),
                menu.getPrice(),
                menu.getShortDescription(),
                menu.getDescription()
        );
    }
}