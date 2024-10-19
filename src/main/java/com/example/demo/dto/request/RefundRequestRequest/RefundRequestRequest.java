package com.example.demo.dto.request.RefundRequestRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequestRequest {
    String orderId;
    String refundReason;
    String refundReasonImage;
}
