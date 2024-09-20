package com.example.demo.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogCreateRequest {
    String image;
    String title;
    String content;
    LocalDate createDate;
    String userId;
}
