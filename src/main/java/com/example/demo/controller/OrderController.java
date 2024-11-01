
package com.example.demo.controller;

import com.example.demo.dto.request.orderRequest.BuyNowRequest;
import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.checkoutResponse.CheckoutResponse;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.service.OrderService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/create/checkout")
    ResponseEntity<ApiResponse<CheckoutResponse>> checkout(@RequestBody CheckoutRequest request) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CheckoutResponse>builder()
                        .message("Checkout successfully!")
                        .result(orderService.checkout(request))
                        .build());
    }

    @PostMapping("/create/buy-now")
    ResponseEntity<ApiResponse<CheckoutResponse>> buyNow(@RequestBody BuyNowRequest request) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CheckoutResponse>builder()
                        .message("Buy now successfully!")
                        .result(orderService.buyNow(request))
                        .build());
    }

    @GetMapping("/get-my-orders")
    ApiResponse<List<CheckoutResponse>> getMyOrders() {
        return ApiResponse.<List<CheckoutResponse>>builder()
                .message("Get my order successfully!")
                .result(orderService.getMyOrders())
                .build();
    }

    @GetMapping("/get-all-orders")
    ApiResponse<List<CheckoutResponse>> getAllOrders() {
        return ApiResponse.<List<CheckoutResponse>>builder()
                .message("Get all order successfully!")
                .result(orderService.getAllOrders())
                .build();

    }

    @GetMapping("/{orderId}")
    ApiResponse<OrderResponse> getOrderById(@PathVariable("orderId") String orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrder(orderId))
                .build();
    }
}

