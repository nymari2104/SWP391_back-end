package com.example.demo.service;

import com.example.demo.configuration.EmailSender;
import com.example.demo.dto.request.orderRequest.BuyNowRequest;
import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.request.orderRequest.GuestCartItemRequest;
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
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
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
    EmailSender emailSender;

    public CheckoutResponse checkout(CheckoutRequest request) throws MessagingException {
        //Check if there already has order for this paymentId
        if(orderRepository.existsByPaymentId(request.getPaymentId()))
            throw new AppException(ErrorCode.PAYMENT_ID_EXISTED);
        //create new order
        Order order = createOrderObject(request);
        Cart cart;
        List<OrderDetail> orderDetails;
        //If Guest buy or not have cart
        if (request.getCartId() == null) {
            Product product;
            OrderDetail orderDetail;
            orderDetails = new ArrayList<>();
            //Check all product he/she buys
            for (GuestCartItemRequest cartItemRequest : request.getCartItems()) {
                //Check exist product
                product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() ->
                        new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                //Decrease stock of product
                decreaseProductStock(product, cartItemRequest.getQuantity());
                //Snapshot product into order detail
                orderDetail = OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .quantity(cartItemRequest.getQuantity())
                        .total(product.getUnitPrice() * cartItemRequest.getQuantity())
                        .build();
                orderDetail.snapshotProduct(product);
                orderDetails.add(orderDetail);
            }
            //If Member or admin buy and have cart
        } else {
            //get cart
            cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            //Map quantity, product of List<CartItem> into List<OrderDetail>
            orderDetails = cart.getCartItems().stream()
                    .map(cartItem -> {
                        OrderDetail orderDetail = orderMapper.toOrderDetail(cartItem);
                        orderDetail.setOrder(order);
                        //Snapshot product
                        orderDetail.snapshotProduct(cartItem.getProduct());
                        //Get product
                        Product product = cartItem.getProduct();
                        //Check if product has enough stock
                        decreaseProductStock(product, cartItem.getQuantity());
                        return orderDetail;
                    })
                    .collect(Collectors.toList());
            //Delete Cart
            User user = order.getUser();
            //Remove relation between cart and user in user before delete cart
            user.setCart(null);
            //Remember Phone and address when user checkout
            user.setAddress(request.getAddress());
            user.setPhone(request.getPhone());
            userRepository.save(user);
            cartRepository.delete(cart);
        }
        order.setOrderDetails(orderDetails);
        //Save Order
        orderRepository.save(order);
        emailSender.sendOrderEmail(order);

        return CheckoutResponse.builder()
                .order(OrderMapper.INSTANCE.toOrderResponse(order))
                .build();
    }

    public CheckoutResponse buyNow(BuyNowRequest request) throws MessagingException {
        //Check if there already has order for this paymentId
        if(orderRepository.existsByPaymentId(request.getPaymentId()))
            throw new AppException(ErrorCode.PAYMENT_ID_EXISTED);
        //Create Order
        Order order = createOrderObject(request);
        //Check exist Product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        //Check if product has enough stock
        decreaseProductStock(product, request.getQuantity());

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(request.getQuantity())
                .total(request.getTotal())
                .build();
        orderDetail.snapshotProduct(product);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        order.setOrderDetails(orderDetails);
        order.setPaymentId(request.getPaymentId());
        //Remember Phone and address when user checkout
        order.getUser().setAddress(order.getAddress());
        order.getUser().setPhone(order.getPhone());
        //Save Order
            orderRepository.save(order);
        //send invoice
        emailSender.sendOrderEmail(order);
        //Map Order to OrderResponse
        return CheckoutResponse.builder()
                .order(OrderMapper.INSTANCE.toOrderResponse(order))
                .build();
    }

    public List<CheckoutResponse> getMyOrders() {
        User user = userService.getCurrentUser();
        List<Order> orders = user.getOrders();
        return orders.stream().map(order -> {
            OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
            CheckoutResponse checkoutResponse = new CheckoutResponse();
            checkoutResponse.setOrder(orderResponse);
            return checkoutResponse;
        }).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CheckoutResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(order -> {
            OrderResponse orderResponse = OrderMapper.INSTANCE.toOrderResponse(order);
            CheckoutResponse checkoutResponse = new CheckoutResponse();
            checkoutResponse.setOrder(orderResponse);
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

    private void decreaseProductStock(Product product, int quantity) {
        //Check if product is active
        if (!product.getStatus()) {
            throw new AppException(ErrorCode.PRODUCT_IS_INACTIVE);
        }
        //Check if product has enough stock
        int productStockRemaining = product.getStock() - quantity;
        if (productStockRemaining < 0) {
            throw new AppException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        }
        //If stock remaining is 0, set status unavailable
        if (productStockRemaining == 0)
            product.setStatus(false);
        //Decrease stock of product
        product.setStock(productStockRemaining);
        productRepository.save(product);
    }

    private Order createOrderObject(CheckoutRequest request) {
        return getOrder(orderMapper.toOrder(request), request.getCartId());
    }

    private Order createOrderObject(BuyNowRequest request) {
        return getOrder(orderMapper.toOrder(request), null);
    }

    private Order getOrder(Order order, String cartId) {
        //Get user who is login
        User user;
        try {
            user = userService.getCurrentUser();
        } catch (Exception e) {
            //Create userId for Guest
            user = userRepository.save(User.builder()
                    .userId("GUEST-" + UUID.randomUUID())
                    .build());
        }
        if (cartId != null) {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            if (!cart.getUser().getUserId().equals(user.getUserId())) {
                throw new AppException(ErrorCode.DID_NOT_OWN_CART);
            }//If member, check this cart is his/her own
        }
        order.setCreateDate(new Date(Instant.now().toEpochMilli()));
        order.setStatus(Status.PENDING.name());
        order.setUser(user);
            return order;
    }
}


