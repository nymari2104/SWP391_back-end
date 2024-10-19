package com.example.demo.dto.request.productRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    String name;
    String image;
    Float unitprice;
    Integer stock;
    String description;
    Boolean status;
    Integer categoryId;
}
