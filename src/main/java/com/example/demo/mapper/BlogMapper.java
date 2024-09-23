package com.example.demo.mapper;

import com.example.demo.dto.request.BlogCreateRequest;
import com.example.demo.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    Blog toBlog(@MappingTarget Blog blog, BlogCreateRequest request);
}
