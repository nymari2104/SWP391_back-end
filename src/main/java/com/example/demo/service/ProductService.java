package com.example.demo.service;

import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public Product createProduct(ProductCreateRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        return productRepository.save(Product.builder()
                .productName(request.getName())
                .image(request.getImage())
                .unitPrice(request.getUnitprice())
                .stock(request.getStock())
                .description(request.getDescription())
                .status(request.isStatus())
                .category(category)
                .build());
    }

    public List<Product> getAllProduct() {
        return productRepository.findAll().stream().toList();
    }

    public Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
