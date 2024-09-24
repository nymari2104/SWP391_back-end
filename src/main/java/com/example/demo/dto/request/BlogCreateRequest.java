package com.example.demo.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogCreateRequest {
    String image;
    String title;
    String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createDate;
    String userId;
}
