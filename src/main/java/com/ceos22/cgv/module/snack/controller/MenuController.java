package com.ceos22.cgv.module.snack.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.movie.dto.MovieListResponse;
import com.ceos22.cgv.module.snack.dto.MenuListResponse;
import com.ceos22.cgv.module.snack.service.MenuService;
import com.ceos22.cgv.response.ApiResponse;
import com.ceos22.cgv.util.MenuCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @RequestMapping("/api/menu")
    public ResponseEntity<ApiResponse<MenuListResponse>> getMenu(
            @RequestParam(required = false) MenuCategory category) {

        ApiResponse<MenuListResponse> response = ApiResponse.<MenuListResponse>builder()
                .response(menuService.getMenusByCategory(category))
                .statusCode(SuccessCode.GET_SUCCESS.getStatusCode())
                .message(SuccessCode.GET_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

}
