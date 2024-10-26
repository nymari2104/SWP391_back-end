package com.example.demo.dto.request.orderRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuestCartItemRequest {
    int productId;
    int quantity;
}
