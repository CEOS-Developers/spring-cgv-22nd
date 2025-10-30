package com.ceos22.cgv.module.snack.service;

import com.ceos22.cgv.module.snack.dto.MenuListResponse;
import com.ceos22.cgv.module.snack.repository.MenuRepository;
import com.ceos22.cgv.common.util.MenuCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public MenuListResponse getMenusByCategory(MenuCategory category) {
        var menus = menuRepository.findAllByCategory(category);
        return MenuListResponse.fromMenus(menus);
    }

}
