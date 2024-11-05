package com.example.demo.controller;

import com.example.demo.dto.request.paymentRequest.BuyNowPaymentRequest;
import com.example.demo.dto.request.paymentRequest.CheckoutPaymentRequest;
import com.example.demo.dto.request.paymentRequest.PaymentRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaypalController {
    PaypalService paypalService;

    @PostMapping("/create/checkout")
    ApiResponse<Map<String, String>> createPaymentForCart(
            @RequestBody CheckoutPaymentRequest request
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPaymentForCart(
                    request,
                    "USD",
                    "paypal",
                    "authorize",
                    "Payment description",
                    cancelUrl,
                    successUrl
            );
            return paypalService.getApprovalUrl(payment);
        } catch (PayPalRESTException e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }

    @PostMapping("/create/buy-now")
    ApiResponse<Map<String, String>> createPaymentForBuyNow(
            @RequestBody BuyNowPaymentRequest request
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success-buy-now";
            Payment payment = paypalService.createPaymentForBuyNow(
                    request,
                    "USD",
                    "paypal",
                    "authorize",
                    "Payment description",
                    cancelUrl,
                    successUrl
            );
            return paypalService.getApprovalUrl(payment);
        } catch (PayPalRESTException e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }

    @GetMapping("/success")
    RedirectView paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) throws PayPalRESTException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (!payment.getState().equals("approved")) {
            return new RedirectView("/payment/error");
        }
        return new RedirectView(
                "http://localhost:5173/payment/success?paymentId=" + paymentId);
    }

    @GetMapping("/success-buy-now")
    RedirectView paymentSuccessBuy(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) throws PayPalRESTException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (!payment.getState().equals("approved")) {
            return new RedirectView("/payment/error");
        }
        return new RedirectView(
                "http://localhost:5173/payment/successbuy?paymentId=" + paymentId);
    }

    @PostMapping("/capture")
    ApiResponse<Void> capture(@RequestBody PaymentRequest request) throws MessagingException {
        paypalService.capturePayment(request.getOrderId());
        return ApiResponse.<Void>builder()
                .message("Capture payment successfully!")
                .build();
    }

    @PostMapping("/void")
    ApiResponse<Void> voidPayment(@RequestBody PaymentRequest request) throws MessagingException {
        paypalService.voidPayment(request.getOrderId());
        return ApiResponse.<Void>builder()
                .message("Void payment successfully!")
                .build();
    }

    @PostMapping("/refund")
    ApiResponse<Void> refund(@RequestBody PaymentRequest request) throws MessagingException {
        paypalService.refundPayment(request.getOrderId());
        return ApiResponse.<Void>builder()
                .message("Refund payment successfully!")
                .build();
    }


    @GetMapping("/cancel")
    RedirectView paymentCancel() {
        return new RedirectView("http://localhost:5173/payment/cancel");
    }

    @GetMapping("/error")
    RedirectView paymentError() {
        return new RedirectView("http://localhost:5173/payment/cancel");
    }
}
