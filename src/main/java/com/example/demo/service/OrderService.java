package com.example.demo.service;

import com.example.demo.dto.request.orderRequest.BuyNowRequest;
import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.response.checkoutResponse.CheckoutResponse;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.entity.*;
import com.example.demo.enums.Role;
import com.example.demo.enums.Status;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

    public CheckoutResponse checkout(CheckoutRequest request) {
        //create new order
        Order order = createOrderObject(request);
        Cart cart;
        List<OrderDetail> orderDetails;
        if (request.getCartId() == null) {
            Product product;
            OrderDetail orderDetail;
            orderDetails = new ArrayList<>();
            for(CheckoutRequest.GuestCartItemRequest cartItemRequest : request.getCartItems()){
                product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() ->
                        new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                orderDetail = OrderDetail.builder()
                        .product(product)
                        .quantity(cartItemRequest.getQuantity())
                        .total(product.getUnitPrice() * cartItemRequest.getQuantity())
                        .build();
                orderDetails.add(orderDetail);
            }
        }else {
            //get cart
            cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            //Map quantity, product of List<CartItem> into List<OrderDetail>
            orderDetails = cart.getCartItems().stream()
                    .map(cartItem -> OrderDetail.builder()
                            .order(order)
                            .product(cartItem.getProduct())
                            .quantity(cartItem.getQuantity())
                            .total(cartItem.getProduct().getUnitPrice() * cartItem.getQuantity())
                            .build())
                    .collect(Collectors.toList());
            //Delete Cart
            User user = cart.getUser();
            //Remove relation between cart and user in user before delete cart
            user.setCart(null);
            userRepository.save(user);
            cartRepository.delete(cart);
        }
        order.setOrderDetails(orderDetails);
        //Save Order
        orderRepository.save(order);

        return CheckoutResponse.builder()
                .orders(OrderMapper.INSTANCE.toOrderResponse(order))
                .build();
    }

    public CheckoutResponse buyNow(BuyNowRequest request) {
        //Check exist Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        //Create Order
        Order order = createOrderObject(request);
        order.setOrderId(request.getPaymentId());
        order.getOrderDetails()
                .add(OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(request.getQuantity())
                        .total(request.getQuantity())
                        .build());
        //Save Order
        orderRepository.save(order);
        //Map Order to OrderResponse
        return CheckoutResponse.builder()
                .orders(OrderMapper.INSTANCE.toOrderResponse(order))
                .build();
    }

    public List<CheckoutResponse> getMyOrder(){
        User user = userService.getCurrentUser();
        List<Order> orders = user.getOrders();
        return  orders.stream().map(order -> {
            OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
            CheckoutResponse checkoutResponse = new CheckoutResponse();
            checkoutResponse.setOrders(orderResponse);
            return checkoutResponse;
        }).collect(Collectors.toList());
    }

    public List<CheckoutResponse> getAllOrder(){
        return orderRepository.findAll().stream().map(order -> {
            OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
            CheckoutResponse checkoutResponse = new CheckoutResponse();
            checkoutResponse.setOrders(orderResponse);
            return checkoutResponse;
        }).collect(Collectors.toList());
    }

    public OrderResponse getOrder(String orderId) {
        //Check if Order exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        User user = userService.getCurrentUser();
        //Check if user is Member and not his/her order
        if (!user.getUserId().equals(order.getUser().getUserId())
                && user.getRole().equals(Role.USER.name()))
            throw new AppException(ErrorCode.DID_NOT_OWN_ORDER);
        return OrderMapper.INSTANCE.toOrderResponse(order);
    }

//    public CheckoutResponse updateOrderStatus(UpdateOrderRequest request){
//        //Find order
//        Order order = orderRepository.findById(request.getOrderId())
//                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
//        //Change order status
//        order.setStatus(request.getStatus());
//        return CheckoutResponse.builder()
//                .orders(OrderMapper.INSTANCE.toOrderResponse(orderRepository.save(order)))//Save the change and Map to CheckoutResponse
//                .build();
//    }

    private Order createOrderObject(CheckoutRequest request) {
        return getOrder(orderMapper.toOrder(request), request.getCartId());
    }

    private Order createOrderObject(BuyNowRequest request) {
        return getOrder(orderMapper.toOrder(request), null);
    }

    private Order getOrder(Order request, String cartId) {
        //Get user who is login
        User user;
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            user = null;
        }
        //Create userId for Guest
        if (user == null)
            user = userRepository.save(User.builder()
                    .userId("GUEST-" + UUID.randomUUID())
                    .build());
        else{
            if (cartId != null) {
                Cart cart = cartRepository.findById(cartId)
                        .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
                if (!cart.getUser().getUserId().equals(user.getUserId())) {
                    throw new AppException(ErrorCode.DID_NOT_OWN_CART);
                }
            }//If member, check this cart is his/her own
        }

        request.setCreateDate(new Date(Instant.now().toEpochMilli()));
        request.setStatus(Status.PENDING.name());
        request.setUser(user);
        try {
            return orderRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.PAYMENT_ID_EXISTED);
        }
    }
}
