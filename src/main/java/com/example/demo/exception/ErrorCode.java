package com.example.demo.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    EMAIL_EXISTED("EMAIL_EXISTED","Email existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION("UNCATEGORIZED_EXCEPTION","Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID("USERNAME_INVALID", "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID("PASSWORD_INVALID","Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY("INVALID_MESSAGE_KEY","Invalid message key", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED("EMAIL_NOT_EXISTED","Email not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("UNAUTHENTICATED","Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("UNAUTHORIZED","You do not have permission", HttpStatus.FORBIDDEN),
    LOGIN_FAIL("LOGIN_FAIL", "Email or password incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID("TOKEN_INVALID", "Token is invalid", HttpStatus.UNAUTHORIZED),
    USER_ID_NOT_EXISTED("USER_ID_NOT_EXISTED", "User id not existed", HttpStatus.NOT_FOUND),
    STOCK_INVALID("STOCK_INVALID", "Stock must be at least 0", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED("CATEGORY_EXISTED", "Category is existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED("CATEGORY_NOT_EXISTED", "Category is not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found", HttpStatus.BAD_REQUEST),
    ;

    private String code;
    private String message;
    private HttpStatusCode statusCode;

}


