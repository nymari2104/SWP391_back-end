package com.example.demo.mapper;

import com.example.demo.dto.request.blogRequest.BlogCreateRequest;
import com.example.demo.dto.request.blogRequest.BlogUpdateRequest;
import com.example.demo.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface BlogMapper {

    Blog toBlog(@MappingTarget Blog blog, BlogCreateRequest request);

    void updateBlog(@MappingTarget Blog blog, BlogUpdateRequest request);
}
