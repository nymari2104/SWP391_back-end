package com.example.demo.mapper;

import com.example.demo.dto.request.koiRequest.KoiUpdateRequest;
import com.example.demo.entity.Koi;
import org.mapstruct.*;

@Mapper
public interface KoiMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateKoi(@MappingTarget Koi koi, KoiUpdateRequest request);
}
