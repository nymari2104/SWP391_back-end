package com.example.demo.dto.response.categoryResponse;

import com.example.demo.dto.response.productResponse.ProductResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    int cateId;
    String cateName;
    List<ProductResponse> products;
}
