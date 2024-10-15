package com.example.demo.mapper;

import com.example.demo.dto.request.orderRequest.BuyNowRequest;
import com.example.demo.dto.request.orderRequest.CheckoutRequest;
import com.example.demo.dto.response.orderResponse.OrderDetailResponse;
import com.example.demo.dto.response.orderResponse.OrderResponse;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order toOrder(CheckoutRequest request);
    Order toOrder(BuyNowRequest request);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "orderDetails", target = "orderDetails")
    OrderResponse toOrderResponse(Order order);

//    @Mapping(source = "product.productId", target = "productId")
//    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.unitPrice", target = "unitPrice")
    @Mapping(source = "product.description", target = "description")
    @Mapping(expression = "java(cartItem.getProduct().getUnitPrice() * cartItem.getQuantity())" , target = "total")
    OrderDetail toOrderDetail(CartItem cartItem);

}
