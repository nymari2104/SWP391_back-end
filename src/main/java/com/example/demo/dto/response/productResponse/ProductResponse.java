package com.example.demo.dto.response.productResponse;

import com.example.demo.dto.response.categoryResponse.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    int productId;
    CategoryResponse category;
    String productName;
    String image;
    float unitPrice;
    int stock;
    String description;
    Boolean status;
}
