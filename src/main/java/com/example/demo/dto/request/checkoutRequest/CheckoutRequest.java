package com.example.demo.dto.request.checkoutRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutRequest {
    String fullname;
    String email;
    String phone;
    String address;
    String cartId;
    float total;
    String paymentId;

}


