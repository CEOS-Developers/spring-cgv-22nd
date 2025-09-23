package com.ceos22.cgv.response;

import com.ceos22.cgv.codes.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private int statusCode;
    private String divisionCode;
    private String message;

    @Builder
    protected ErrorResponse(final ErrorCode statusCode){
        this.statusCode = statusCode.getStatusCode();
        this.divisionCode = statusCode.getDivisionCode();
        this.message = statusCode.getMessage();
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }
}
