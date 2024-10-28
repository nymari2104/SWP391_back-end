package com.example.demo.dto.request.RefundRequestRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandleRefundRequestRequest {
    String orderId;
}
