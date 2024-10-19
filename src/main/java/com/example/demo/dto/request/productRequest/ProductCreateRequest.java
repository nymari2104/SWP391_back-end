package com.example.demo.dto.request.productRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {
    String name;
    String image;
    float unitprice;
    int stock;
    String description;
    boolean status;
    int categoryId;
}
