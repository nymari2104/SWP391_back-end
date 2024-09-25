package com.example.demo.controller;

import com.example.demo.dto.request.WaterParamCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.WaterParam;
import com.example.demo.service.WaterParamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/water-param")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WaterParamController {

    WaterParamService waterParamService;

    @PostMapping("/create")
    ApiResponse<WaterParam> createWaterParam(@RequestBody WaterParamCreateRequest request) {
        return ApiResponse.<WaterParam>builder()
                .message("Create water parameter log successfully")
                .result(waterParamService.createWaterParam(request))
                .build();
    }

}
