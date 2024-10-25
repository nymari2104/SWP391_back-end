package com.example.demo.controller;

import com.example.demo.dto.request.orderRequest.BuyNowRequest;
import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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
                @RequestBody CheckoutRequest request
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
            @RequestBody BuyNowRequest request
    ) {
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/successbuy";
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
    ) throws PayPalRESTException{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(!payment.getState().equals("approved")) {
                 return new RedirectView("/payment/error");
            }
        return new RedirectView(
                "http://localhost:5173/payment/success?paymentId=" + paymentId);
    }

    @PostMapping("/capture")
    ApiResponse<Void> capture(@RequestBody CheckoutRequest request){
        paypalService.capturePayment(request.getPaymentId());
        return ApiResponse.<Void>builder()
                .message("Capture payment successfully!")
                .build();
    }

    @PostMapping("/void")
    ApiResponse<Void> voidPayment(@RequestBody CheckoutRequest request){
        paypalService.voidPayment(request.getPaymentId());
        return ApiResponse.<Void>builder()
                .message("Void payment successfully!")
                .build();
    }

    @GetMapping("/refund")
    ApiResponse<Void> refund(@RequestParam("paymentId") String paymentId){
        paypalService.refundPayment(paymentId);
        return ApiResponse.<Void>builder()
                .message("Refund payment successfully!")
                .build();
    }


    @GetMapping("/cancel")
    String paymentCancel(){
        return "cancel";
    }

    @GetMapping("/error")
    String paymentError(){
        return "error";
    }
}
