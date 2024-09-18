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
    EMAIL_EXISTED(1001,"Email existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1002, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003,"Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(1004,"Invalid message key", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1005,"Email not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You do not have permission", HttpStatus.FORBIDDEN),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}


