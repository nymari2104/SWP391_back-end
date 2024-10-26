package com.example.demo.controller;

import com.example.demo.dto.request.waterParamRequest.WaterParamCreateRequest;
import com.example.demo.dto.request.waterParamRequest.WaterParamUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.WaterParam;
import com.example.demo.service.WaterParamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/water-param")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WaterParamController {

    WaterParamService waterParamService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<WaterParam>> createWaterParam(@RequestBody WaterParamCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<WaterParam>builder()
                        .message("Create water parameter log successfully")
                        .result(waterParamService.createWaterParam(request))
                        .build());
    }

    @PutMapping("/update/{waterParamId}")
    ResponseEntity<Void> updateWaterParam(@PathVariable String waterParamId ,@RequestBody WaterParamUpdateRequest request) {
        waterParamService.updateWaterParam(waterParamId, request);
        return ResponseEntity.noContent().build();
    }

}
