package com.example.demo.dto.request.productRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
