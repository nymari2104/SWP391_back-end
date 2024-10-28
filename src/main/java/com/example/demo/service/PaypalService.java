package com.example.demo.service;

import com.example.demo.dto.request.orderRequest.GuestCartItemRequest;
import com.example.demo.dto.request.paymentRequest.BuyNowPaymentRequest;
import com.example.demo.dto.request.paymentRequest.CheckoutPaymentRequest;
import com.example.demo.dto.response.ApiResponse;
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
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
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
    protected String PAYPAL_VOID_API = "https://api-m.sandbox.paypal.com/v1/payments/authorization/";
    @NonFinal
    protected String PAYPAL_ACCESS_TOKEN_API = "https://api-m.sandbox.paypal.com/v1/oauth2/token";

    public Payment createPaymentForCart(
            CheckoutPaymentRequest request,
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

        List<Item> items = new ArrayList<>();

        if (request.getCartId() == null) {
            Product product;
            for (GuestCartItemRequest cartItem : request.getCartItems()) {
                //Check product exist
                product = productRepository.findById(cartItem.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
                items.add(item(product, cartItem.getQuantity(), currency));
            }
        } else {
            Cart cart = cartRepository.findById(request.getCartId())
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            for (CartItem cartItem : cart.getCartItems()) {
                items.add(item(
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        currency));
            }
        }
        return createPayment(method, intent, description, cancelUrl, successUrl, amount, items);
    }

    public Payment createPaymentForBuyNow(
            BuyNowPaymentRequest request,
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

        List<Item> items = new ArrayList<>();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        //Check if product is active
        if (!product.getStatus())
            throw new AppException(ErrorCode.PRODUCT_IS_INACTIVE);
        //Check if product has enough stock
        if (request.getQuantity() > product.getStock())
            throw new AppException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        items.add(item(product, request.getQuantity(), currency));
        return createPayment(method, intent, description, cancelUrl, successUrl, amount, items);
    }

    private Payment createPayment(String method,
                                  String intent,
                                  String description,
                                  String cancelUrl,
                                  String successUrl,
                                  Amount amount,
                                  List<Item> items)
            throws PayPalRESTException {
        ItemList itemList = new ItemList();
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

    private Item item(Product product, int quantity, String currency) {
        //Check if product is active
        if (!product.getStatus())
            throw new AppException(ErrorCode.PRODUCT_IS_INACTIVE);
        //Check if product has enough stock
        if (quantity > product.getStock())
            throw new AppException(ErrorCode.PRODUCT_NOT_ENOUGH_STOCK);
        Item item = new Item();
        item.setName(product.getProductName());
        item.setCurrency(currency);
        item.setPrice(String.format(Locale.forLanguageTag(currency), "%.2f", product.getUnitPrice()));
        item.setQuantity(String.valueOf(quantity));

        return item;
    }

    public ApiResponse<Map<String, String>> getApprovalUrl(Payment payment) {
        Map<String, String> response = new HashMap<>();
        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                response.put("approval_url", link.getHref());
                break;
            }
        }
        return ApiResponse.<Map<String, String>>builder()
                .message("Approved URL")
                .result(response)
                .build();
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public void refundPayment(String orderId) {
        //Check if order exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = createPayment(order.getPaymentId());
        String captureId;
        try {
            captureId = payment.path("transactions").get(0)
                    .path("related_resources").get(1)
                    .path("capture").path("id").asText();
        } catch (Exception e) {
            throw new AppException(ErrorCode.ORDER_IS_PENDING);
        }
        // Trả về phản hồi từ PayPal API
        try {
            restTemplate.exchange(
                    PAYPAL_REFUND_API + captureId + "/refund",
                    HttpMethod.POST,
                    setBody(payment),
                    Void.class);
        } catch (RestClientException e) {
            throw new AppException(ErrorCode.PAYMENT_ID_INVALID);
        }
//        //Set status before refunded
        orderRepository.save(order);
    }

//    @Async
    @PreAuthorize("hasRole('ADMIN')")
    public void capturePayment(String orderId) {
        //Check if order exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = createPayment(order.getPaymentId());
        String authorizationId = payment.path("transactions").get(0)
                .path("related_resources").get(0)
                .path("authorization").path("id").asText();
        // Trả về phản hồi từ PayPal API
        try {
            restTemplate.exchange(
                    PAYPAL_CAPTURE_API + authorizationId + "/capture",
                    HttpMethod.POST,
                    setBody(payment),
                    Void.class);
        } catch (RestClientException e) {
            log.error("error {}", e.getMessage());
            throw new AppException(ErrorCode.ORDER_ALREADY_APPROVED);
        }
        //Set status before capture
        order.setStatus(Status.APPROVED.name());
        orderRepository.save(order);
    }


    public void voidPayment(String orderId) {
        //Check if order exist
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        JsonNode payment = createPayment(order.getPaymentId());
        String authorizationId = payment.path("transactions").get(0)
                .path("related_resources").get(0)
                .path("authorization").path("id").asText();
        HttpEntity<String> entity = new HttpEntity<>(setHeader());
        // Trả về phản hồi từ PayPal API
        try {
            restTemplate.exchange(
                    PAYPAL_VOID_API + authorizationId + "/void",
                    HttpMethod.POST,
                    entity,
                    Void.class);
        } catch (RestClientException e) {
            throw new AppException(ErrorCode.ORDER_ALREADY_REJECTED);
        }
        //Set status before Void
        order.setStatus(Status.REJECTED.name());
        //Return product stock when order is rejected
        order.getOrderDetails().forEach(orderDetail -> {
            Product product = orderDetail.getProduct();
            product.setStock(product.getStock() + orderDetail.getQuantity());
            productRepository.save(product);
        });
        orderRepository.save(order);
    }

    private JsonNode createPayment(String paymentId) {
        HttpEntity<JsonNode> entity = new HttpEntity<>(setHeader());
        // Trả về phản hồi từ PayPal API
        ResponseEntity<JsonNode> response;
        try {
            response = restTemplate.exchange(
                    PAYPAL_PAYMENTS_API + paymentId,
                    HttpMethod.GET,
                    entity,
                    JsonNode.class);
        } catch (RestClientException e) {
            throw new AppException(ErrorCode.PAYMENT_ID_INVALID);
        }
        return response.getBody();
    }

    private HttpEntity<Map<String, Object>> setBody(JsonNode payment) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> amount = new HashMap<>();
        amount.put("currency", "USD");
        amount.put("total", payment.path("transactions").get(0).path("amount").path("total").asText());
        body.put("amount", amount);
        body.put("is_final_capture", true);
        return new HttpEntity<>(body, setHeader());
    }

    private String getAccessToken() {
        String credentials = apiContext.getClientID() + ":" + apiContext.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(PAYPAL_ACCESS_TOKEN_API, request, JsonNode.class);

        if (!(response.getStatusCode() == HttpStatus.OK))
            throw new AppException(ErrorCode.FAIL_TO_RETRIEVE_TOKEN);
        return Objects.requireNonNull(response.getBody()).path("access_token").asText();
    }

    private HttpHeaders setHeader() {
        //createOrderDetail headers with Bearer token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + getAccessToken());
        httpHeaders.set("Content-Type", "application/json");
        return httpHeaders;
    }
}
