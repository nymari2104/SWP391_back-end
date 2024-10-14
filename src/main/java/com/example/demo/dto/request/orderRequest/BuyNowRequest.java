package com.example.demo.dto.request.orderRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuyNowRequest {
    String fullname;
    String email;
    String phone;
    String address;
    float total;
    int productId;
    int quantity;
    String paymentId;
}
