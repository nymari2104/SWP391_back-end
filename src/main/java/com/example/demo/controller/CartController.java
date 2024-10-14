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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @PostMapping("/create")
    ApiResponse<CartResponse> createCart(@RequestBody CreateCartRequest request) {

        return ApiResponse.<CartResponse>builder()
                .message("Create cart successfully")
                .result(cartService.createCart(request.getUserId()))
                .build();
    }

    @PostMapping("/{cartId}/add/{productId}")
    ApiResponse<CartResponse> addToCart(@PathVariable("cartId") String cartId,
                                        @PathVariable("productId") int productId,
                                        @RequestBody AddToCartRequest request) {

        return ApiResponse.<CartResponse>builder()
                .message("Add to cart successfully")
                .result(cartService.addToCart(cartId, productId, request))
                .build();
    }

    @PutMapping("/{cartId}/update/{cartItemId}")
    ApiResponse<Cart> updateQuantity(@PathVariable String cartId,
                                     @PathVariable String cartItemId,
                                     @Valid @RequestBody UpdateQuantityRequest request) {

        return ApiResponse.<Cart>builder()
                .message("Update quantity successfully")
                .result(cartService.updateCartItemQuantity(cartId, cartItemId, request))
                .build();
    }

    @DeleteMapping("delete/{cartItemId}")
    ApiResponse<Boolean> deleteCartItem(@PathVariable String cartItemId) {

        cartService.removeCartItem(cartItemId);

        return ApiResponse.<Boolean>builder()
                .message("Delete cart item successfully")
                .result(true)
                .build();
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
