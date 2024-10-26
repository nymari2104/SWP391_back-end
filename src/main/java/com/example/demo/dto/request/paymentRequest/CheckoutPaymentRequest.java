package com.example.demo.dto.request.paymentRequest;

import com.example.demo.dto.request.orderRequest.GuestCartItemRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutPaymentRequest {
    String cartId;
    float total;
    List<GuestCartItemRequest> cartItems;
}
