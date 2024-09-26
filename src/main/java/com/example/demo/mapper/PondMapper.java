package com.example.demo.mapper;

import com.example.demo.dto.request.pondRequest.PondUpdateRequest;
import com.example.demo.entity.Pond;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface PondMapper {

    void updatePond(@MappingTarget Pond pond, PondUpdateRequest request);
}
