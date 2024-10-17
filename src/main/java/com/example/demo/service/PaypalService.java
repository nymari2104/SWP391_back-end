package com.example.demo.service;

import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.enums.Status;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaypalService {
    APIContext apiContext;
    CartRepository cartRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;

    @NonFinal
    protected RestTemplate restTemplate = new RestTemplate();

    @NonFinal
    protected String PAYPAL_PAYMENTS_API = "https://api-m.sandbox.paypal.com/v1/payments/payment/";
    @NonFinal
    protected String PAYPAL_REFUND_API = "https://api-m.sandbox.paypal.com/v1/payments/capture/";
    @NonFinal
    protected String PAYPAL_CAPTURE_API = "https://api-m.sandbox.paypal.com/v1/payments/authorization/";
    @NonFinal
    protected  String PAYPAL_VOID_API = "https://api-m.sandbox.paypal.com/v1/payments/authorization/";

    public Payment createPayment(
            CheckoutRequest request,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    )
            throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", request.getTotal()));

        Item item;
        List<Item> items = new ArrayList<>();

        ItemList itemList = new ItemList();

        if (request.getCartId() == null) {
            Product product;
            for (CheckoutRequest.GuestCartItemRequest cartItem : request.getCartItems()){
                item = new Item();
                product = productRepository.findById(cartItem.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                item.setName(product.getProductName());
                item.setCurrency(currency);
                item.setPrice(String.format(Locale.forLanguageTag(currency),
                        "%.2f", product.getUnitPrice()));
                item.setQuantity(String.valueOf(cartItem.getQuantity()));
                items.add(item);
            }
        }else {
            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

            for (CartItem cartItem : cart.getCartItems()) {
                item = new Item();
                item.setName(cartItem.getProduct().getProductName());
                item.setCurrency(currency);
                item.setPrice(String.format(Locale.forLanguageTag(currency), "%.2f", cartItem.getProduct().getUnitPrice()));
                item.setQuantity(String.valueOf(cartItem.getQuantity()));

                items.add(item);
            }
        }
        itemList.setItems(items);
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setItemList(itemList);


        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Transactions listTransaction = new Transactions();
        listTransaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    public void refundPayment(String paymentId, String accessToken){
        //Find Order by paymentId
        Order order = orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = getPayment(paymentId, accessToken);
        String captureId = payment.path("transactions").get(0)
                    .path("related_resources").get(0)
                    .path("capture").path("id").asText();
        // Trả về phản hồi từ PayPal API
        restTemplate.exchange(
                PAYPAL_REFUND_API + captureId + "/refund",
                HttpMethod.GET,
                setBody(accessToken, payment),
                Capture.class);
        //Set status before refunded
        order.setStatus(Status.REFUNDED.name());
        //Return stock if order is refunded
        order.getOrderDetails().forEach(orderDetail -> {
            Product product = orderDetail.getProduct();
            product.setStock(product.getStock() + orderDetail.getQuantity());
            productRepository.save(product);
        });
        orderRepository.save(order);
    }



    public void capturePayment(String paymentId, String accessToken){
        //Find Order by paymentId
        Order order = orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = getPayment(paymentId, accessToken);
        String authorizationId = payment.path("transactions").get(0)
                    .path("related_resources").get(0)
                    .path("authorization").path("id").asText();
        // Trả về phản hồi từ PayPal API
        restTemplate.exchange(
                PAYPAL_CAPTURE_API + authorizationId + "/capture",
                HttpMethod.POST,
                setBody(accessToken, payment),
                JsonNode.class);
        //Set status before capture
        order.setStatus(Status.APPROVED.name());
        orderRepository.save(order);
    }

    public void voidPayment(String paymentId, String accessToken){
        //Find Order by paymentId
        Order order = orderRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = getPayment(paymentId, accessToken);
        String authorizationId = payment.path("transactions").get(0)
                .path("related_resources").get(0)
                .path("authorization").path("id").asText();
        HttpEntity<String> entity = new HttpEntity<>(setHeader(accessToken));
        // Trả về phản hồi từ PayPal API
        restTemplate.exchange(
                PAYPAL_VOID_API + authorizationId + "/void",
                HttpMethod.POST,
                entity,
                String.class);
        //Set status before Void
        order.setStatus(Status.REJECTED.name());
        //Return product stock if order is rejected
        order.getOrderDetails().forEach(orderDetail -> {
            Product product = orderDetail.getProduct();
            product.setStock(product.getStock() + orderDetail.getQuantity());
            productRepository.save(product);
        });
        orderRepository.save(order);
    }

    public JsonNode getPayment(String paymentId, String accessToken){
        HttpEntity<JsonNode> entity = new HttpEntity<>(setHeader(accessToken));
        // Trả về phản hồi từ PayPal API
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                PAYPAL_PAYMENTS_API + paymentId,
                HttpMethod.GET,
                entity,
                JsonNode.class);
        return response.getBody();
    }

    private  HttpEntity<Map<String, Object>> setBody(String accessToken, JsonNode payment) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> amount = new HashMap<>();
        amount.put("currency", "USD");
        amount.put("total", payment.path("transactions").get(0).path("amount").path("total").asText());
        body.put("amount", amount);
        body.put("is_final_capture", true);
        return new HttpEntity<>(body ,setHeader(accessToken));
    }

    private HttpHeaders setHeader(String accessToken){
        //createOrderDetail headers with Bearer token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        httpHeaders.set("Content-Type", "application/json");

       return httpHeaders;
    }
}
