package com.example.demo.service;

import com.example.demo.dto.request.WaterParamCreateRequest;
import com.example.demo.entity.Pond;
import com.example.demo.entity.WaterParam;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.WaterParamMapper;
import com.example.demo.repository.PondRepository;
import com.example.demo.repository.WaterParamRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WaterParamService {

    WaterParamRepository waterParamRepository;
    PondRepository pondRepository;
    WaterParamMapper waterParamMapper;

    public WaterParam createWaterParam(WaterParamCreateRequest request) {

        Pond pond = pondRepository.findById(request.getPondId())
                .orElseThrow(() -> new AppException(ErrorCode.POND_NOT_FOUND));

        WaterParam waterParam = new WaterParam();
        waterParamMapper.toWaterParam(waterParam, request);
        waterParam.setPond(pond);

        return waterParamRepository.save(waterParam);
    }
}
