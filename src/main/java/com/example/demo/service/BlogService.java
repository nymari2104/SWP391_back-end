package com.example.demo.service;

import com.example.demo.dto.request.BlogCreateRequest;
import com.example.demo.entity.Blog;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.BlogRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogService {

    BlogRepository blogRepository;
    UserRepository userRepository;

    public Blog createBlog(BlogCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return blogRepository.save(Blog.builder()
                .title(request.getTitle())
                .image(request.getImage())
                .content(request.getContent())
                .createDate(request.getCreateDate())
                .user(user)
                .build());

    }
}
