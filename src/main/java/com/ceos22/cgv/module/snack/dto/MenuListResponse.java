package com.ceos22.cgv.module.snack.dto;

import com.ceos22.cgv.module.snack.domain.Menu;
import java.util.List;

public record MenuListResponse(List<MenuResponse> menus) {
    public static MenuListResponse fromMenus(List<Menu> menus) {
        return new MenuListResponse(menus.stream().map(MenuResponse::fromMenu).toList());
    }
}
