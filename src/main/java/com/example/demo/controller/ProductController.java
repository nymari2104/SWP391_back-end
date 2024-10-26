package com.example.demo.controller;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.productResponse.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Product>builder()
                        .result(productService.createProduct(request))
                        .message("Create product successfully")
                        .build());
    }

    @GetMapping("/list")
    ApiResponse<List<Product>> getAllProducts() {
        return ApiResponse.<List<Product>>builder()
                .message("Get all products successfully")
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ProductResponse> getProduct(@PathVariable("id") int id) {
        return ApiResponse.<ProductResponse>builder()
                .message("Get product successfully")
                .result(productService.getProduct(id))
                .build();
    }

    @GetMapping("/shop")
    ApiResponse<List<ProductResponse>> getAllActiveProducts() {
        return ApiResponse.<List<ProductResponse>>builder()
                .message("Get all active product successfully")
                .result(productService.getAllActiveProduct())
                .build();
    }

    @PutMapping("/update/{productId}")
    ResponseEntity<Void> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductUpdateRequest request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("delete/{productId}")
    ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
