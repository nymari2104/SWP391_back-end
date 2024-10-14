package com.example.demo.dto.response.orderResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    String orderId;
    String paymentId;
    String userId;
    String status;
    String phone;
    String address;
    String fullname;
    String email;
    Date createDate;
    List<OrderDetailResponse> orderDetails;
}
