package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.movie.dto.request.DirectorCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.DirectorResponse;
import com.ceos22.cgv_clone.domain.movie.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/directors")
@RequiredArgsConstructor
public class AdminDirectorController {

    private final DirectorService directorService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<DirectorResponse>> createDirector(
            @Valid @RequestBody DirectorCreateRequest request
    ) {
        DirectorResponse createdDirector = directorService.createDirector(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, createdDirector);
    }
}