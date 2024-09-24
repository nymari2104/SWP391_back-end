package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiCreateRequest {
    String name;
    String image;
    Boolean sex;
    String type;
    String origin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createDate;
    int pondId;
}
