package com.ceos22.cgv_clone.global.apiPayload.code;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;                 // 에러 상태 코드
    private String divisionCode;        // 에러 구분 코드
    private String resultMsg;           // 에러 메시지
    private List<FieldError> errors;    // 상세 에러 메시지
    private String reason;              // 에러 이유


    @Builder
    public ErrorResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.divisionCode = code.getDivisionCode();
        this.resultMsg = code.getMessage();
        this.errors = new ArrayList<>();
    }

    @Builder
    public ErrorResponse(ErrorCode code, String reason) {
        this(code);
        this.reason = reason;
    }

    @Builder
    public ErrorResponse(ErrorCode code, List<FieldError> errors) {
        this(code);
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(ErrorCode code, BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
