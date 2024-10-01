package com.example.demo.service;

import com.example.demo.dto.request.waterParamRequest.WaterParamCreateRequest;
import com.example.demo.dto.request.waterParamRequest.WaterParamUpdateRequest;
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

import java.util.Date;

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
        waterParam.setCreateDate(new Date());

        return waterParamRepository.save(waterParam);
    }

    public WaterParam updateWaterParam(String waterParamId,WaterParamUpdateRequest request) {
        WaterParam waterParam = waterParamRepository.findById(waterParamId)
                .orElseThrow(() -> new AppException(ErrorCode.WATER_PARAM_NOT_FOUND));
        waterParamMapper.updateWaterParam(waterParam, request);
        waterParam.setCreateDate(new Date());
        return waterParamRepository.save(waterParam);
    }
}
