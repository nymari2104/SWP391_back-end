package com.example.demo.service;

import com.example.demo.dto.request.blogRequest.BlogCreateRequest;
import com.example.demo.dto.request.blogRequest.BlogUpdateRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Blog getBlog(String id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));
    }

//    public List<Blog> getAllBlog(int page, int limit) {
//        return blogRepository.findAll(PageRequest.of(page, limit)).getContent();
//    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll().stream().toList();
    }

    public Blog updateBlog(String blogId, BlogUpdateRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new AppException(ErrorCode.BLOG_NOT_FOUND));

        blogMapper.updateBlog(blog,request);

        return blogRepository.save(blog);
    }

    public void deleteBlog(String blogId) {
        blogRepository.deleteById(blogId);
    }
}
