package com.example.demo.service;

import com.example.demo.dto.request.RefundRequestRequest.HandleRefundRequestRequest;
import com.example.demo.dto.request.RefundRequestRequest.RefundRequestRequest;
import com.example.demo.dto.response.RefundRequestResponse.RefundRequestResponse;
import com.example.demo.entity.Order;
import com.example.demo.entity.RefundRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.RefundRequestMapper;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.RefundRequestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefundRequestService {
    RefundRequestRepository refundRequestRepository;
    OrderRepository orderRepository;
    RefundRequestMapper refundRequestMapper;
    UserService userService;

    public RefundRequestResponse makeARefund(RefundRequestRequest request) {
        //Check exist order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        //Check if already refund
        if(order.getRefundRequest() != null)
            throw new AppException(ErrorCode.ALREADY_REQUEST_REFUNDED);
        //map request to RefundRequest Object to save
        RefundRequest refundRequest = refundRequestMapper.toRefundRequest(request);
        refundRequest.setOrder(order);
        refundRequest.setStatus(Status.PENDING.name());
        return refundRequestMapper.toRefundRequestResponse(refundRequestRepository.save(refundRequest));//map to response
    }

    @PreAuthorize("hasRole('ADMIN')")
    public RefundRequestResponse handleRefund(HandleRefundRequestRequest request, String status) {
        //Check refund request exist
        RefundRequest refundRequest = refundRequestRepository.findById(request.getRefundRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.REFUND_REQUEST_NOT_FOUND));
        //Check if refund request already be handled
        if (!refundRequest.getStatus().equals(Status.PENDING.name()))
            throw new AppException(ErrorCode.ALREADY_HANDLE_REFUND_REQUEST);
        //map request to RefundRequest Object to save
        refundRequestMapper.toRefundRequest(request);
        //Check if request was approved or rejected
        if(status.equals(Status.APPROVED.name()))
            callPayPalRefundApi(refundRequest.getOrder().getPaymentId());
        refundRequest.setStatus(status);
        //Get info admin who pending the refund
        User user = userService.getCurrentUser();
        refundRequest.setAdminId(user.getUserId());
        return refundRequestMapper.toRefundRequestResponse(refundRequestRepository.save(refundRequest));//map to response
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<RefundRequestResponse> getAllRefundRequests() {
        List<RefundRequest> refundRequests = refundRequestRepository.findAll();
        return refundRequests.stream()
                .map(refundRequestMapper::toRefundRequestResponse)
                .collect(Collectors.toList());
    }

    private void callPayPalRefundApi(String paymentId){
        String refundEndpoint = "http://localhost:8080/payment/refund?paymentId=" + paymentId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity(refundEndpoint, Void.class);
    }
}
