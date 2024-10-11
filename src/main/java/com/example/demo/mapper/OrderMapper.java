package com.example.demo.mapper;

import com.example.demo.dto.request.checkoutRequest.BuyNowRequest;
import com.example.demo.dto.request.checkoutRequest.CheckoutRequest;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.entity.Order;
import org.mapstruct.*;

@Mapper
public interface OrderMapper {
    Order toOrder(CheckoutRequest request);
    Order toOrder(BuyNowRequest request);

    OrderResponse toOrderResponse(Order order);

}
