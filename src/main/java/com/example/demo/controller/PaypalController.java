package com.example.demo.controller;

import com.example.demo.dto.request.checkoutRequest.CheckoutRequest;
import com.example.demo.dto.response.ApiResponse;
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
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaypalController {
    PaypalService paypalService;

    @NonFinal
    protected String ACCESS_TOKEN =
            "A21AAIIJnTVqWx0KythuKE_18Q6oqa8TaBYrUDeBB-3jQLWei1z9Ijpl2ppt3W8k-dSdwe4uyGtc13cGUzPImThp6gFp7yjzw";

    @GetMapping("/create")
    public RedirectView createPayment(
//            @RequestBody CheckoutRequest request
    ){
        CheckoutRequest request = new CheckoutRequest();
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment(
                    request,
                    "USD",
                    "paypal",
                    "authorize",
                    "Payment description",
                    cancelUrl,
                    successUrl
            );
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return new RedirectView(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/success")
    public ApiResponse<String > paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) throws PayPalRESTException{
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(!payment.getState().equals("approved")) {
                 throw new RuntimeException("Payment failed");
            }
            log.info("Payment successful{}",payment);
//            String authorizationId = payment.getTransactions().getFirst()
//                                            .getRelatedResources().getFirst()
//                                            .getAuthorization()
//                                            .getId();
        return ApiResponse.<String>builder()
                .message("Pay successfully!")
                .result(paymentId)
                .build();
    }

    @PostMapping("/capture")
    public String capture(@RequestBody CheckoutRequest request){
        paypalService.capture(request.getPaymentId(), ACCESS_TOKEN);
        return "Capture successfully!";
    }

    @PostMapping("/refund")
    public String refund(@RequestBody String request){
        paypalService.refund(request, ACCESS_TOKEN);
        return "Refund successfully";
    }

    @GetMapping("/cancel")
    public String paymentCancel(){
        return "cancel";
    }

    @GetMapping("/error")
    public String paymentError(){
        return "error";
    }
}
