package com.ceos22.cgv.common.codes;

import lombok.Getter;

@Getter
public enum SuccessCode {

    // 조회 성공
    GET_SUCCESS(200, "GET_SUCCESS"),

    // 조회 성공
    LOGIN_SUCCESS(200, "LOGIN_SUCCESS"),

    // 삭제 성공
    DELETE_SUCCESS(200, "DELETE_SUCCESS"),

    // 삽입 성공
    INSERT_SUCCESS(201, "INSERT_SUCCESS"),

    // 수정 성공
    UPDATE_SUCCESS(204, "UPDATE_SUCCESS"),

    ;

     private final int statusCode;

     private final String message;

     SuccessCode(final int statusCode, final String message) {
         this.statusCode = statusCode;
         this.message = message;
     }
}
