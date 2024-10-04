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
    EMAIL_INVALID("EMAIL_INVALID", "Email is invalid", HttpStatus.BAD_REQUEST),
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
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", HttpStatus.BAD_REQUEST),
    EMAIL_OTP_INVALID("EMAIL_OTP_INVALID", "Email OTP is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_OTP_EXPIRED("EMAIL_OTP_EXPIRED", "Email OTP is expired", HttpStatus.BAD_REQUEST),
    BLOG_NOT_FOUND("BLOG_NOT_FOUND", "Blog not found", HttpStatus.NOT_FOUND),
    BLANK_EMAIL("BLANK_EMAIL", "Email cannot be blank", HttpStatus.BAD_REQUEST),
    KOI_NOT_FOUND("KOI_NOT_FOUND", "Koi not found", HttpStatus.NOT_FOUND),
    MATCH_OLD_PASSWORD("MATCHED_OLD_PASSWORD","Password matched the old", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD("WRONG_PASSWORD", "Current password is not matched", HttpStatus.BAD_REQUEST),
    WATER_PARAM_NOT_FOUND("WATER_PARAM_NOT_FOUND", "Water param not found", HttpStatus.NOT_FOUND),
    POND_NOT_FOUND("POND_NOT_FOUND", "Pond not found", HttpStatus.NOT_FOUND),
    LOG_NOT_FOUND("LOG_NOT_FOUND", "Log not found", HttpStatus.NOT_FOUND),
    ;

     String code;
     String message;
     HttpStatusCode statusCode;

}


