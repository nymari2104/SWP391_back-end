package com.example.demo.controller;

import com.example.demo.dto.request.KoiGrowthLogCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.KoiGrowthLog;
import com.example.demo.service.KoiGrowthLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/koi-growth-log")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiGrowthLogController {

    KoiGrowthLogService koiGrowthLogService;

    @PostMapping("/create")
    ApiResponse<KoiGrowthLog> createKoiGrowthLog(@RequestBody KoiGrowthLogCreateRequest request) {

        return ApiResponse.<KoiGrowthLog>builder()
                .message("Create Koi's log successfullfy")
                .result(koiGrowthLogService.createKoiGrowthLog(request))
                .build();
    }
}
