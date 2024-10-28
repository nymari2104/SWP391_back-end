package com.example.demo.controller;

import com.example.demo.dto.request.cartRequest.AddToCartRequest;
import com.example.demo.dto.request.cartRequest.CreateCartRequest;
import com.example.demo.dto.request.cartRequest.UpdateQuantityRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.cartResponse.CartResponse;
import com.example.demo.entity.Cart;

import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<CartResponse>> createCart(@RequestBody CreateCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CartResponse>builder()
                        .message("Create cart successfully")
                        .result(cartService.createCart(request.getUserId()))
                        .build());
    }

    @PutMapping("/{cartId}/add/{productId}")
    ApiResponse<CartResponse> addToCart(@PathVariable("cartId") String cartId,
                                        @PathVariable("productId") int productId,
                                        @RequestBody AddToCartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .message("Add to cart successfully")
                .result(cartService.addToCart(cartId, productId, request))
                .build();
    }

    @PutMapping("/{cartId}/update/{cartItemId}")
    ResponseEntity<Void> updateQuantity(@PathVariable String cartId,
                                  @PathVariable String cartItemId,
                                  @Valid @RequestBody UpdateQuantityRequest request) {
        cartService.updateCartItemQuantity(cartId, cartItemId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete/{cartItemId}")
    ResponseEntity<Void> deleteCartItem(@PathVariable String cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-cart")
    ApiResponse<Void> deleteCart(@RequestBody String cartId){
        cartService.deleteCart(cartId);
        return ApiResponse.<Void>builder()
                .message("Delete cart successfully")
                .build();
    }

//    @GetMapping("/{cartId}")
//    ApiResponse<CartResponse> getCart(@PathVariable String cartId) {
//
//        return ApiResponse.<CartResponse>builder()
//                .message("Get cart Successfully")
//                .result(cartService.getCart(cartId))
//                .build();
//    }

    @GetMapping("/user/{userId}")
    ApiResponse<Cart> getCartByUser(@PathVariable String userId) {

        return ApiResponse.<Cart>builder()
                .message("Get cart successfully")
                .result(cartService.getCartByUser(userId))
                .build();
    }
}
