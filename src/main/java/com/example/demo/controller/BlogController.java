package com.example.demo.controller;

import com.example.demo.dto.request.blogRequest.BlogCreateRequest;
import com.example.demo.dto.request.blogRequest.BlogUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.blogResponse.BlogResponse;
import com.example.demo.entity.Blog;
import com.example.demo.service.BlogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping("/create")
    ApiResponse<Blog> createBlog(@RequestBody BlogCreateRequest request) {

//        String image = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Blog>builder()
                .result(blogService.createBlog(BlogCreateRequest.builder()
                        .userId(request.getUserId())
                        .title(request.getTitle())
                        .content(request.getContent())
                        .image(request.getImage())
                        .createDate(new Date())
                        .build()))
                .build();

    }

    @GetMapping("/list")
    ApiResponse<List<BlogResponse>> getAllBlogs() {
        return ApiResponse.<List<BlogResponse>>builder()
                .message("Get all blogs successfully")
                .result(blogService.getAllBlogs())
                .build();
    }

    @PutMapping("/update/{blogId}")
    ApiResponse<Blog> updateBlog(@PathVariable int blogId, @RequestBody BlogUpdateRequest request) {
        return ApiResponse.<Blog>builder()
                .message("Update Blog successfully")
                .result(blogService.updateBlog(blogId, request))
                .build();
    }

    @DeleteMapping("/delete/{blogId}")
    ApiResponse<Boolean> deleteBlog(@PathVariable int blogId) {
        blogService.deleteBlog(blogId);
        return ApiResponse.<Boolean>builder()
                .message("Delete blog successfully")
                .result(true)
                .build();
    }

    @GetMapping("/{blogId}")
    ApiResponse<BlogResponse> getBlog(@PathVariable int blogId) {
        return ApiResponse.<BlogResponse>builder()
                .message("Get blog successfully")
                .result(blogService.getBlog(blogId))
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<BlogResponse>> getUserBlog(@PathVariable String userId) {
        return ApiResponse.<List<BlogResponse>>builder()
                .message("Get blog successfully")
                .result(blogService.getUserBlog(userId))
                .build();
    }

}
