package com.example.demo.dto.response.RefundRequestResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundRequestResponse {
    String refundRequestId;
    String orderId;
    String status;
    String refundReason;
    String refundReasonImage;
    String adminId;
    String refundResponse;
    Date createDate;
}
