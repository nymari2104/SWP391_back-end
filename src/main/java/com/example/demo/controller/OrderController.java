package com.example.demo.controller;

import com.example.demo.dto.request.checkoutRequest.BuyNowRequest;
import com.example.demo.dto.request.checkoutRequest.CheckoutRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/create/checkout")
    ApiResponse<OrderResponse> checkout(@RequestBody CheckoutRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .message("Checkout successfully!")
                .result(orderService.checkout(request))
                .build();
    }

    @PostMapping("/create/buy-now")
    ApiResponse<OrderResponse> buyNow(@RequestBody BuyNowRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .message("Buy now successfully!")
                .result(orderService.buyNow(request))
                .build();
    }

    @GetMapping("/get-my-order")
    ApiResponse<List<Order>> getMyOrder() {
        return ApiResponse.<List<Order>>builder()
                .message("Get my order successfully!")
                .result(orderService.getMyOrder())
                .build();
    }
}
