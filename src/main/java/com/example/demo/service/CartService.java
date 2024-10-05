package com.example.demo.service;

import com.example.demo.dto.request.cartRequest.AddToCartRequest;
import com.example.demo.dto.request.cartRequest.UpdateQuantityRequest;
import com.example.demo.dto.response.cartResponse.CartItemResponse;
import com.example.demo.dto.response.cartResponse.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    public CartResponse createCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(cartRepository.existsByUser(user))
            throw new AppException(ErrorCode.CART_ALREADY_EXISTED);

        Cart cart = cartRepository.save(Cart.builder()
                .user(user)
                .createDate(new Date())
                .build());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .createDate(cart.getCreateDate())
                .build();
    }

    public CartResponse addToCart(String cartId, int productId, AddToCartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        boolean productExistsInCart = false;
        if (!cart.getCartId().isEmpty()) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getProduct().getProductId() == productId) {
                    item.setQuantity(request.getQuantity() + item.getQuantity());
                    cartItemRepository.save(item);
                    productExistsInCart = true;
                    break;
                }
            }
        }
        if (!productExistsInCart) {
            CartItem cartItem = cartItemRepository.save(CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build());
            cart.getCartItems().add(cartItem);
        }

        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProduct().getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cartId)
                .userId(cart.getUser().getUserId())
                .createDate(cart.getCreateDate())
                .items(itemResponses)
                .build();
    }

    public Cart updateCartItemQuantity(String cartId, String cartItemId, UpdateQuantityRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CARTITEM_NOT_FOUND));

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        return cart;
    }

    public void removeCartItem(String cartItemId) {
        if(cartItemRepository.existsById(cartItemId))
            cartItemRepository.deleteById(cartItemId);
        else
            throw new AppException(ErrorCode.CARTITEM_NOT_FOUND);
    }

    public CartResponse getCart(String cartId) {
        // Tìm giỏ hàng theo cartId
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Chuyển đổi Cart thành CartResponse
        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
                .map(item -> CartItemResponse.builder()
                        .cartItemId(item.getCartItemId())
                        .productId(item.getProduct().getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .createDate(cart.getCreateDate())
                .items(itemResponses) // Thêm danh sách các item đã chuyển đổi
                .build();
    }

    public Cart getCartByUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Cart cart = user.getCart();

        if(cart == null) throw new AppException(ErrorCode.CART_NOT_FOUND);

        return cart;

//        List<CartItemResponse> itemResponses = cart.getCartItems().stream()
//                .map(item -> CartItemResponse.builder()
//                        .cartItemId(item.getCartItemId())
//                        .productId(item.getProduct().getProductId())
//                        .quantity(item.getQuantity())
//                        .build())
//                .collect(Collectors.toList());
//
//        return CartResponse.builder()
//                .cartId(cart.getCartId())
//                .userId(cart.getUser().getUserId())
//                .createDate(cart.getCreateDate())
//                .items(itemResponses) // Thêm danh sách các item đã chuyển đổi
//                .build();
    }
}
