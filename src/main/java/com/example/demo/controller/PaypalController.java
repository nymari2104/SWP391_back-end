package com.example.demo.controller;

import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaypalController {
    PaypalService paypalService;

    @NonFinal
    protected String ACCESS_TOKEN =
            "A21AAKI24BLo6m4g7KGtPPuVmwGiu6cRMvFjdLwJa-Xyt2aJ2nTEvJqJcUejnVDCPiDL9b4LTGhLfVnHywQtrvMFNIqJiC0pg";

    @PostMapping("/create")
    ApiResponse<Map<String, String>> createPayment(
                @RequestBody CheckoutRequest request
    ) {
        try {
            String cancelUrl = "http://localhost:5173/payment/cancel";
            String successUrl = "http://localhost:5173/payment/success";
            Payment payment = paypalService.createPayment(
                    request,
                    "USD",
                    "paypal",
                    "authorize",
                    "Payment description",
                    cancelUrl,
                    successUrl
            );
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
        } catch (PayPalRESTException e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }

    @GetMapping("/success")
    ApiResponse<String> paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) throws PayPalRESTException{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(!payment.getState().equals("approved")) {
                 throw new RuntimeException("Payment failed");
            }
            log.info("Payment successful{}",payment);
        return ApiResponse.<String>builder()
                .message("Pay successfully!")
                .result(paymentId)
                .build();
    }

    @PostMapping("/capture")
    ApiResponse<Void> capture(@RequestBody CheckoutRequest request){
        paypalService.capturePayment(request.getPaymentId(), ACCESS_TOKEN);
        return ApiResponse.<Void>builder()
                .message("Capture payment successfully!")
                .build();
    }

    @PostMapping("/void")
    ApiResponse<Void> voidPayment(@RequestBody CheckoutRequest request){
        paypalService.voidPayment(request.getPaymentId(), ACCESS_TOKEN);
        return ApiResponse.<Void>builder()
                .message("Void payment successfully!")
                .build();
    }

    @PostMapping("/refund")
    ApiResponse<Void> refund(@RequestBody String request){
        paypalService.refundPayment(request, ACCESS_TOKEN);
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
