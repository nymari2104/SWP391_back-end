package com.example.demo.dto.response.orderResponse;

import com.example.demo.entity.OrderDetail;
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
@JsonInclude(JsonInclude.Include.NON_NULL)//not show field which is null
public class OrderResponse {
    String orderId;
    String userId;
    String status;
    String phone;
    String address;
    String fullname;
    String email;
    Date createDate;
    List<OrderDetail> orderDetails;
}
