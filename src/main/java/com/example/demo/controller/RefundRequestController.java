package com.example.demo.controller;

import com.example.demo.dto.request.RefundRequestRequest.RefundRequestRequest;
import com.example.demo.dto.request.RefundRequestRequest.HandleRefundRequestRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.RefundRequestResponse.RefundRequestResponse;
import com.example.demo.enums.Status;
import com.example.demo.service.RefundRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefundRequestController {
    RefundRequestService refundRequestService;

    @PostMapping
    ApiResponse<RefundRequestResponse> makeARefund(@RequestBody RefundRequestRequest request) {
        return ApiResponse.<RefundRequestResponse>builder()
                .message("Make a refund request successfully!")
                .result(refundRequestService.makeARefund(request))
                .build();
    }

    @PostMapping("/approved")
    ApiResponse<RefundRequestResponse> approveRefund(@RequestBody HandleRefundRequestRequest request) {
        return ApiResponse.<RefundRequestResponse>builder()
                .message("Approve a refund request successfully!")
                .result(refundRequestService.handleRefund(request, Status.APPROVED.name()))
                .build();
    }

    @PostMapping("/rejected")
    ApiResponse<RefundRequestResponse> rejectRefund(@RequestBody HandleRefundRequestRequest request) {
        return ApiResponse.<RefundRequestResponse>builder()
                .message("Reject a refund request successfully!")
                .result(refundRequestService.handleRefund(request, Status.REJECTED.name()))
                .build();
    }

    @GetMapping
    ApiResponse<List<RefundRequestResponse>> getAllRefundRequests() {
        return ApiResponse.<List<RefundRequestResponse>>builder()
                .message("Get all refund requests Successfully!")
                .result(refundRequestService.getAllRefundRequests())
                .build();
    }
}
