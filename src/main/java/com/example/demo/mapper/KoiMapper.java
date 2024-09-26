package com.example.demo.mapper;

import com.example.demo.dto.request.koiRequest.KoiUpdateRequest;
import com.example.demo.entity.Koi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface KoiMapper {


    void updateKoi(@MappingTarget Koi koi, KoiUpdateRequest request);
}
