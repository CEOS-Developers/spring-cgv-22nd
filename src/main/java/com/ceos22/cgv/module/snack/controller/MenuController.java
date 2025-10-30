package com.ceos22.cgv.module.snack.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.snack.dto.MenuListResponse;
import com.ceos22.cgv.module.snack.service.MenuService;
import com.ceos22.cgv.common.response.ApiResponse;
import com.ceos22.cgv.common.util.MenuCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "메뉴 목록 조회", description = "메뉴 카테고리로 분류된 스낵/음료 목록을 조회합니다. 카테고리가 없으면 전체 목록을 반환합니다.")
    @GetMapping("/menu")
    public ResponseEntity<ApiResponse<MenuListResponse>> getMenu(
            @RequestParam(required = false) MenuCategory category) {

        ApiResponse<MenuListResponse> response = ApiResponse.of(
                menuService.getMenusByCategory(category),
                SuccessCode.GET_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

}
