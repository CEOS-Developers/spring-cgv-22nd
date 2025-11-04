package com.ceos22.cgv_clone.global.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 공통
    BAD_REQUEST_ERROR(400, "Bad Request"),
    NOT_VALID_ERROR(400, "Validation Failed"),
    REQUEST_BODY_MISSING_ERROR(400, "Request body missing"),
    MISSING_REQUEST_HEADER_ERROR(400, "Missing request header"),
    INVALID_TYPE_VALUE(400, "Invalid type value"),
    MISSING_REQUEST_PARAMETER_ERROR(400, "Missing request parameter"),
    UNAUTHORIZED_ERROR(401, "Unauthorized"),
    FORBIDDEN_ERROR(403, "Forbidden"),
    NOT_FOUND_ERROR(404, "Not Found"),
    NO_RESOURCE_FOUND_ERROR(404, "No resource found"),
    METHOD_NOT_ALLOWED_ERROR(405, "Method not allowed"),
    CONFLICT_ERROR(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),

    // User
    USER_NOT_FOUND(404, "User not found"),

    // Movie
    MOVIE_NOT_FOUND(404, "Movie not found"),

    // Showtime
    SHOWTIME_NOT_FOUND(404, "Showtime not found"),

    // Theater
    THEATER_NOT_FOUND(404, "Theater not found"),

    // Favorite
    FAVORITE_NOT_FOUND(404, "Favorite not found"),
    FAVORITE_DUPLICATED(409, "Favorite already exists"),

    // Product
    PRODUCT_NOT_FOUND(404, "Product not found"),

    // Order
    ORDER_NOT_FOUND(404, "Order not found"),
    FORBIDDEN_ORDER_ACCESS(403, "You can access only your own order"),

    // Payment
    PAYMENT_REQUESTED(409, "Payment already requested"),
    PAYMENT_ALREADY_PAID(409, "Payment already completed"),
    PAYMENT_FAILED(502, "Payment failed"),
    PAYMENT_CANCEL_FAILED(502, "Payment cancel failed"),
    // 외부 PG 결제 실패

    // Stock
    STOCK_NOT_FOUND(404, "Stock not found"),
    STOCK_SHORTAGE(409, "Insufficient stock"),

    // Ticket
    TICKET_NOT_FOUND(404, "Ticket not found"),
    SEAT_ALREADY_RESERVED(409, "Seat already reserved"),
    SEAT_QUERY_FAILED(500, "Seat query failed"),

    FORBIDDEN_TICKET_ACCESS(403, "You can access only your own ticket"),
    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
