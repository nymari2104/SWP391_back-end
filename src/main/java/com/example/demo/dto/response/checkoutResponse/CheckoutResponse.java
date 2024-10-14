package com.example.demo.dto.response.checkoutResponse;

import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)//not show field which is null
public class CheckoutResponse {
    OrderResponse orders;
}
