package com.example.demo.service;


import com.example.demo.dto.request.blogRequest.BlogCreateRequest;
import com.example.demo.dto.request.blogRequest.BlogUpdateRequest;
import com.example.demo.dto.response.blogResponse.BlogResponse;
import com.example.demo.entity.Blog;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.repository.BlogRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlogService {

    BlogRepository blogRepository;
    UserRepository userRepository;
    BlogMapper blogMapper;

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

    public BlogResponse getBlog(int id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
        return BlogResponse.builder()
                .blogId(blog.getBlogId())
                .image(blog.getImage())
                .title(blog.getTitle())
                .content(blog.getContent())
                .createDate(blog.getCreateDate())
                .fullname(blog.getUser().getFullname())
                .userId(blog.getUser().getUserId())
                .build();
    }

//    public List<Blog> getAllBlog(int page, int limit) {
//        return blogRepository.findAll(PageRequest.of(page, limit)).getContent();
//    }

    public List<BlogResponse> getAllBlogs() {

        return blogRepository.findAll()
                .stream()
                .map(blog -> {
                    return BlogResponse.builder()
                            .blogId(blog.getBlogId())
                            .image(blog.getImage())
                            .title(blog.getTitle())
                            .content(blog.getContent())
                            .createDate(blog.getCreateDate())
                            .fullname(blog.getUser().getFullname())
                            .build();
                }).collect(Collectors.toList());
    }

    public Blog updateBlog(int blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        blogMapper.updateBlog(blog, request);

        return blogRepository.save(blog);
    }

    public void deleteBlog(int blogId) {
        blogRepository.deleteById(blogId);
    }

    //    @PreAuthorize("hasRole('USER')")
    public List<BlogResponse> getUserBlog(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getBlogs().stream().map(
                blog -> {
                    return BlogResponse.builder()
                            .blogId(blog.getBlogId())
                            .image(blog.getImage())
                            .title(blog.getTitle())
                            .content(blog.getContent())
                            .createDate(blog.getCreateDate())
                            .fullname(blog.getUser().getFullname())
                            .build();
                }
        ).toList();
    }
}
