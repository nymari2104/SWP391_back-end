package com.example.demo.mapper;

import com.example.demo.dto.request.RefundRequestRequest.RefundRequestRequest;
import com.example.demo.dto.request.RefundRequestRequest.HandleRefundRequestRequest;
import com.example.demo.dto.response.RefundRequestResponse.RefundRequestResponse;
import com.example.demo.entity.RefundRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RefundRequestMapper {
    RefundRequest toRefundRequest(RefundRequestRequest refundRequestRequest);
    RefundRequest toRefundRequest(HandleRefundRequestRequest rejectRefundRequest);
    @Mapping(source = "order.orderId", target = "orderId")
    RefundRequestResponse toRefundRequestResponse(RefundRequest refundRequest);

}
