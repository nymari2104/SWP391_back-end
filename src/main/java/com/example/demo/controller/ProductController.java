package com.example.demo.controller;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping("/create")
    ApiResponse<Product> createProduct(@RequestBody ProductCreateRequest request){

        return ApiResponse.<Product>builder()
                .result(productService.createProduct(request))
                .message("Create product successfully")
                .build();
    }

    @GetMapping("/list")
    ApiResponse<List<Product>> getAllProducts() {
        return ApiResponse.<List<Product>>builder()
                .message("Get all products successfully")
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<Product> getProduct(@PathVariable("id") int id) {
        return ApiResponse.<Product>builder()
                .message("Get product successfully")
                .result(productService.getProduct(id))
                .build();
    }

    @GetMapping("/shop")
    ApiResponse<List<Product>> getAllActiveProducts() {
        return ApiResponse.<List<Product>>builder()
                .message("Get all active product successfully")
                .result(productService.getAllActiveProduct())
                .build();
    }

    @PutMapping("/update/{productId}")
    ApiResponse<Product> updateProduct(@PathVariable("productId") int productId, @RequestBody ProductUpdateRequest request) {
        return ApiResponse.<Product>builder()
                .message("Update product successfully")
                .result(productService.updateProduct(productId, request))
                .build();
    }

    @DeleteMapping("delete/{productId}")
    ApiResponse<Boolean> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<Boolean>builder()
                .message("Delete product successfully")
                .result(true)
                .build();
    }


}
