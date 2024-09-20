package com.example.demo.controller;

import com.example.demo.configuration.ImageResizer;
import com.example.demo.dto.request.BlogCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Blog;
import com.example.demo.service.BlogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Filter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogController {

    BlogService blogService;

    @PostMapping("/create")
    ApiResponse<Blog> createBlog(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("user") String user,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("createDate") LocalDate createDate) throws IOException {

        String image = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Blog>builder()
                .result(blogService.createBlog(BlogCreateRequest.builder()
                        .userId(user)
                        .title(title)
                        .content(content)
                        .image(image)
                        .createDate(createDate)
                        .build()))
                .build();

    }

}
