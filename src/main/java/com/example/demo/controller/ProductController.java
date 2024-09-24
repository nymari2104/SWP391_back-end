package com.example.demo.controller;

import com.example.demo.configuration.ImageResizer;
import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping("/create")
    ApiResponse<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("stock") int stock,
            @RequestParam("description") String description,
            @RequestParam("unitprice") float unitprice,
            @RequestParam("status") boolean status,
            @RequestParam("category") int id) throws IOException {

        String resizedImageBase64 = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Product>builder()
                .result(productService.createProduct(ProductCreateRequest.builder()
                        .name(name)
                        .image(resizedImageBase64)
                        .description(description)
                        .stock(stock)
                        .unitprice(unitprice)
                        .status(status)
                        .categoryId(id)
                        .build()))
                .message("Create product successfully")
                .build();
    }

    @GetMapping("/list")
    ApiResponse<List<Product>> getAllProduct() {
        return ApiResponse.<List<Product>>builder()
                .message("Get all products successfully")
                .result(productService.getAllProduct())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<Product> getProduct(@PathVariable("id") String id) {
        return ApiResponse.<Product>builder()
                .message("Get product successfully")
                .result(productService.getProduct(id))
                .build();
    }


}
