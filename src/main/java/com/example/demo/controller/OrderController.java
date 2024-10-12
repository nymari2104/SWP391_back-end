package com.example.demo.controller;

import com.example.demo.dto.request.checkoutRequest.BuyNowRequest;
import com.example.demo.dto.request.checkoutRequest.CheckoutRequest;
import com.example.demo.dto.request.checkoutRequest.UpdateOrderRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.checkoutResponse.CheckoutResponse;
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
    ApiResponse<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        return ApiResponse.<CheckoutResponse>builder()
                .message("Checkout successfully!")
                .result(orderService.checkout(request))
                .build();
    }

    @PostMapping("/create/buy-now")
    ApiResponse<CheckoutResponse> buyNow(@RequestBody BuyNowRequest request) {
        return ApiResponse.<CheckoutResponse>builder()
                .message("Buy now successfully!")
                .result(orderService.buyNow(request))
                .build();
    }

    @GetMapping("/get-my-order")
    ApiResponse<List<CheckoutResponse>> getMyOrder() {
        return ApiResponse.<List<CheckoutResponse>>builder()
                .message("Get my order successfully!")
                .result(orderService.getMyOrder())
                .build();
    }

    @GetMapping("/get-all-order")
    ApiResponse<List<CheckoutResponse>> getAllOrder() {
        return ApiResponse.<List<CheckoutResponse>>builder()
                .message("Get all order successfully!")
                .result(orderService.getAllOrder())
                .build();
    }

    @PutMapping("/update-order-status")
    ApiResponse<CheckoutResponse> updateOrderStatus(@RequestBody UpdateOrderRequest request) {
            return ApiResponse.<CheckoutResponse>builder()
                    .message("Update order status successfully!")
                    .result(orderService.updateOrderStatus(request))
                    .build();
        }
    }

