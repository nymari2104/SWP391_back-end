package com.example.demo.service;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.dto.response.categoryResponse.CategoryResponse;
import com.example.demo.dto.response.productResponse.ProductResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public List<Product> getAllProduct() {
        return productRepository.findAll().stream().toList();
    }

//    public Product getProduct(int id) {
//        return productRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
//    }

    public ProductResponse getProduct(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

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
    }

    public List<ProductResponse> getAllActiveProduct() {
        List<Product> products = productRepository.findByStatusTrue().stream().toList();
        return products.stream().map(product -> {
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
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void updateProduct(int productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        productMapper.updateProduct(product, request);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            product.setCategory(category);
        }

        productRepository.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(int productId) {
        try {
            productRepository.deleteById(productId);
        } catch (Exception e) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}
