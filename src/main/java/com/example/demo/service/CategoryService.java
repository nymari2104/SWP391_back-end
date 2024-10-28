package com.example.demo.service;

import com.example.demo.dto.response.categoryResponse.CategoryResponse;
import com.example.demo.dto.response.productResponse.ProductResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().toList();
    }

    public CategoryResponse getCategory(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        List<ProductResponse> productResponseList = category.getProducts().stream()
                .filter(Product::getStatus)
                .map(product -> {
                    CategoryResponse categoryResponse = CategoryResponse.builder()
                            .cateId(product.getCategory().getCateId())
                            .cateName(product.getCategory().getCateName())
                            .build();
                    return ProductResponse.builder()
                            .productId(product.getProductId())
                            .category(categoryResponse)
                            .productName(product.getProductName())
                            .description(product.getDescription())
                            .image(product.getImage())
                            .stock(product.getStock())
                            .status(product.getStatus())
                            .unitPrice(product.getUnitPrice())
                            .build();
                }).toList();

        return CategoryResponse.builder()
                .cateId(category.getCateId())
                .cateName(category.getCateName())
                .products(productResponseList)
                .build();
    }


}
