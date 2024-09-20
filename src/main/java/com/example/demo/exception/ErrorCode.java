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
    EMAIL_EXISTED(400,"Email existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(500,"Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(400, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400,"Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(400,"Invalid message key", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(404,"Email not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403,"You do not have permission", HttpStatus.FORBIDDEN),
    LOGIN_FAIL(400, "Email or password incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(401, "Token is invalid", HttpStatus.UNAUTHORIZED),
    USER_ID_NOT_EXISTED(404, "User id not existed", HttpStatus.NOT_FOUND),
    STOCK_INVALID(400, "Stock must be at least 0", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(400, "Category is existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(400, "Category is not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(400, "Product not found", HttpStatus.BAD_REQUEST),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}


