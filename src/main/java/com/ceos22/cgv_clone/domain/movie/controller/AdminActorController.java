package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.movie.dto.request.ActorCreateRequest;
import com.ceos22.cgv_clone.domain.movie.dto.response.ActorResponse;
import com.ceos22.cgv_clone.domain.movie.service.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/actors")
@RequiredArgsConstructor
public class AdminActorController {

    private final ActorService actorService;

    @PostMapping
    public ResponseEntity<ApiResponse<ActorResponse>> createActor(
            @Valid @RequestBody ActorCreateRequest request
    ) {
        ActorResponse createdActor = actorService.createActor(request);
        return ApiResponse.success(SuccessCode.CREATE_SUCCESS, createdActor);
    }
}