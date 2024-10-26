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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<Blog>> createBlog(@RequestBody BlogCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Blog>builder()
                .message("Create blog successfully")
                .result(blogService.createBlog(request))
                .build());

    }

    @GetMapping("/list")
    ApiResponse<List<BlogResponse>> getAllBlogs() {
        return ApiResponse.<List<BlogResponse>>builder()
                .message("Get all blogs successfully!")
                .result(blogService.getAllBlogs())
                .build();
    }

    @PutMapping("/update/{blogId}")
    ResponseEntity<Void> updateBlog(@PathVariable int blogId, @RequestBody BlogUpdateRequest request) {
        blogService.updateBlog(blogId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{blogId}")
    ResponseEntity<Void> deleteBlog(@PathVariable int blogId) {
        blogService.deleteBlog(blogId);
        return ResponseEntity.noContent().build();
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
