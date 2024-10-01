package com.example.demo.mapper;

import com.example.demo.dto.request.waterParamRequest.WaterParamCreateRequest;
import com.example.demo.dto.request.waterParamRequest.WaterParamUpdateRequest;
import com.example.demo.entity.WaterParam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WaterParamMapper {

    WaterParam toWaterParam(@MappingTarget WaterParam waterParam, WaterParamCreateRequest request);

    void updateWaterParam(@MappingTarget WaterParam waterParam, WaterParamUpdateRequest request);

}
