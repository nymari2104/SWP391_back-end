package com.example.demo.service;

import com.example.demo.dto.request.checkoutRequest.BuyNowRequest;
import com.example.demo.dto.request.checkoutRequest.CheckoutRequest;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.entity.*;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    UserRepository userRepository;
    CartRepository cartRepository;
    UserService userService;
    OrderMapper orderMapper;
    ProductRepository productRepository;

    public OrderResponse checkout(CheckoutRequest request) {
        //create new order
        Order order = createOrderObject(request);
        //get cart
        Cart cart = cartRepository.findById(request.getCartId()).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        //Map quantity, product of List<CartItem> into List<OrderDetail>
        List<OrderDetail> orderDetails = cart.getCartItems().stream()
                .map(cartItem -> OrderDetail.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .total(cartItem.getProduct().getUnitPrice() * cartItem.getQuantity())
                        .build())
                .collect(Collectors.toList());

        order.setOrderDetails(orderDetails);
        order.setPaymentId(request.getPaymentId());

        orderRepository.save(order);
        //delete cart after order
//        cartRepository.delete(cart);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setUserId(order.getUser().getUserId());
        return orderResponse;
    }

    public OrderResponse buyNow(BuyNowRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Order order = createOrderObject(request);
        order.setPaymentId(request.getPaymentId());
        order.getOrderDetails()
                .add(OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(request.getQuantity())
                        .total(request.getQuantity())
                        .build());

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setUserId(order.getUser().getUserId());
        return orderResponse;
    }

    public List<Order> getMyOrder(){
        User user = userRepository.findById(userService.getCurrentUser().getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return user.getOrders();
    }

    private Order createOrderObject(CheckoutRequest request) {
        User user;
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            user = forGuest();
        }
        Order order = orderMapper.toOrder(request);
        order.setCreateDate(new Date());
        order.setStatus("PROCESSING");
        order.setUser(user);
        return orderRepository.save(order);
    }

    private Order createOrderObject(BuyNowRequest request) {
        User user;
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            user = forGuest();
        }
        Order order = orderMapper.toOrder(request);
        order.setCreateDate(new Date());
        order.setStatus("PROCESSING");
        order.setUser(user);
        return orderRepository.save(order);
    }

    private User forGuest(){
        return userRepository.save(User.builder()
                .userId("GUEST-" + UUID.randomUUID())
                .build());
    }
}
