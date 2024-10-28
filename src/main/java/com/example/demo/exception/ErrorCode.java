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
    CATEGORY_EXISTED("CATEGORY_EXISTED", "Category is existed", HttpStatus.CONFLICT),
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
    CART_NOT_FOUND("CART_NOT_FOUND", "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("CART_ITEM_NOT_FOUND", "Cart item not found", HttpStatus.NOT_FOUND),
    QUANTITY_GREATER_THAN_0("QUANTITY_GREATER_THAN_0", "Quantity must equal or greater than 0", HttpStatus.BAD_REQUEST),
    CART_ALREADY_EXISTED("CART_ALREADY_EXISTED", "Cart is already existed", HttpStatus.BAD_REQUEST),
    PAYMENT_FAILED("PAYMENT_FAILED", "Payment failed", HttpStatus.BAD_REQUEST),
    PAYMENT_ID_EXISTED("PAYMENT_ID_EXISTED", "Payment id existed", HttpStatus.BAD_REQUEST),
    SENDER_EMAIL_IS_NOT_AUTHORIZED("SENDER_EMAIL_IS_NOT_AUTHORIZED","Sender email is not authorized", HttpStatus.UNAUTHORIZED),
    DID_NOT_OWN_CART("DID_NOT_OWN_CART", "You did not own this cart", HttpStatus.FORBIDDEN),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND","Order not found",HttpStatus.NOT_FOUND),
    DID_NOT_OWN_ORDER("DID_NOT_OWN_ORDER", "You did not own this order", HttpStatus.FORBIDDEN),
    PRODUCT_NOT_ENOUGH_STOCK("PRODUCT_NOT_ENOUGH_STOCK","Product not enough stock", HttpStatus.BAD_REQUEST),
    ALREADY_REQUEST_REFUNDED("ALREADY_REQUESTED_REFUND","Order already be requested a refund", HttpStatus.BAD_REQUEST),
    REFUND_REQUEST_NOT_FOUND("REFUND_REQUEST_NOT_FOUND","Refund request not found", HttpStatus.NOT_FOUND),
    ALREADY_APPROVED_REFUND_REQUEST("ALREADY_APPROVED_REFUND_REQUEST","The order is already refunded", HttpStatus.BAD_REQUEST),
    PAYMENT_ID_INVALID("PAYMENT_ID_INVALID","Payment id is invalid", HttpStatus.BAD_REQUEST),
    FAIL_TO_RETRIEVE_TOKEN("FAIL_TO_RETRIEVE_TOKEN","Fail to retrieve token", HttpStatus.BAD_REQUEST),
    USER_INACTIVE("USER_INACTIVE","User is inactive", HttpStatus.FORBIDDEN),
    LOGGED_BY_GOOGLE("LOGGED_BY_GOOGLE","Email is logged by google cannot change password", HttpStatus.BAD_REQUEST),
    ORDER_IS_PENDING("ORDER_IS_PENDING","The order is still pending", HttpStatus.BAD_REQUEST),
    ORDER_IS_REJECTED("ORDER_IS_REJECTED","The order is rejected", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_APPROVED("ORDER_ALREADY_APPROVED","Order is already approved", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_REJECTED("ORDER_ALREADY_REJECTED","Order is already rejected", HttpStatus.BAD_REQUEST),
    QUANTITY_GREATER_THAN_STOCK("QUANTITY_GREATER_THAN_STOCK", "Quantity add to cart must be less than or equal stock", HttpStatus.BAD_REQUEST),
    PRODUCT_IS_INACTIVE("PRODUCT_IS_INACTIVE","Product is inactive", HttpStatus.BAD_REQUEST),
    ;

     String code;
     String message;
     HttpStatusCode statusCode;

}


