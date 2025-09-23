package com.ceos22.spring_boot.domain.movie.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;
import com.ceos22.spring_boot.domain.movie.dto.request.CreateMovieReq;
import com.ceos22.spring_boot.domain.movie.dto.response.CreateMovieRes;
import com.ceos22.spring_boot.domain.movie.dto.response.DeleteMovieRes;
import com.ceos22.spring_boot.domain.movie.dto.response.MovieRes;
import com.ceos22.spring_boot.domain.movie.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	//1. 새로운 데이터를 create하도록 요청하는 API
	@Operation(summary = "영화 등록 API", description = "사용자가 제목/상영시간/장르/관람등급/개봉일을 입력해 영화를 등록합니다.")
	@PostMapping("/")
	public CommonResponse<CreateMovieRes> createMovie(
		@Parameter(description = "제목/상영시간/장르/관람등급/개봉일을 담은 요청 객체", required = true)
		@Valid @RequestBody CreateMovieReq req
	){
		CreateMovieRes res = movieService.createMovie(req);
		return CommonResponse.success(res);
	}

	//2. 모든 데이터를 가져오는 API
	@Operation(summary = "영화 전체 조회 API", description = "등록된 모든 영화를 조회합니다.")
	@GetMapping("/")
	public CommonResponse<List<MovieRes>> findAllMovies(){
		List<MovieRes> res = movieService.findAllMovies();
		return CommonResponse.success(res);
	}
	//3. 특정 데이터를 가져오는 API
	@Operation(summary = "영화 단건 조회 API", description = "영화 ID를 입력하여 특정 영화를 조회합니다.")
	@GetMapping("/{id}")
	public CommonResponse<MovieRes> findMovie(
		@Parameter(description = "조회할 영화 ID", required = true)
		@PathVariable Long id
	) {
		MovieRes res = movieService.findMovieById(id);
		return CommonResponse.success(res);
	}

	//4. 특정 데이터를 삭제하는 API
	@Operation(summary = "영화 삭제 API", description = "영화 ID를 입력하여 특정 영화를 삭제합니다.")
	@DeleteMapping("/{id}")
	public CommonResponse<DeleteMovieRes> deleteMovie(
		@Parameter(description = "삭제할 영화 ID", required = true)
		@PathVariable Long id
	) {
		return CommonResponse.success(movieService.deleteMovieById(id));
	}
}
