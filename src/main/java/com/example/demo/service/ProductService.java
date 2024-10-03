package com.example.demo.service;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
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

    public Product getProduct(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<Product> getAllActiveProduct() {
        return productRepository.findByStatusTrue().stream().toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(int productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        product.setProductName(request.getName());
        product.setImage(request.getImage());
        product.setStock(request.getStock());
        product.setUnitPrice(request.getUnitprice());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(int productId) {
        if (productRepository.existsById(productId))
            productRepository.deleteById(productId);
        else
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
    }

}
