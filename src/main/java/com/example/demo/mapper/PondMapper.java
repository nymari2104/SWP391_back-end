package com.example.demo.mapper;

import com.example.demo.dto.request.pondRequest.PondUpdateRequest;
import com.example.demo.entity.Pond;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface PondMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePond(@MappingTarget Pond pond, PondUpdateRequest request);
}
