package com.example.demo.mapper;

import com.example.demo.dto.request.blogRequest.BlogCreateRequest;
import com.example.demo.dto.request.blogRequest.BlogUpdateRequest;
import com.example.demo.entity.Blog;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface BlogMapper {

    Blog toBlog(@MappingTarget Blog blog, BlogCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);
}
