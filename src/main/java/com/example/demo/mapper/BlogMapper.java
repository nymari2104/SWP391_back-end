package com.example.demo.mapper;

import com.example.demo.dto.request.BlogCreateRequest;
import com.example.demo.dto.request.BlogUpdateRequest;
import com.example.demo.dto.response.BlogResponse;
import com.example.demo.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface BlogMapper {

    Blog toBlog(@MappingTarget Blog blog, BlogCreateRequest request);

    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);


    BlogResponse toBlogResponse(Blog blog);
}
