package com.example.demo.mapper;

import com.example.demo.dto.request.KoiGrowthLogCreateRequest;
import com.example.demo.entity.KoiGrowthLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KoiGrowthLogMapper {

    KoiGrowthLog toKoiGrowthLog(@MappingTarget KoiGrowthLog koiGrowthLog, KoiGrowthLogCreateRequest request);
}
