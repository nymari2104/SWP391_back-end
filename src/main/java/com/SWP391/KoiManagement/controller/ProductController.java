package com.SWP391.KoiManagement.controller;

import com.SWP391.KoiManagement.configuration.ImageResizer;
import com.SWP391.KoiManagement.dto.request.ProductCreateRequest;
import com.SWP391.KoiManagement.dto.response.ApiResponse;
import com.SWP391.KoiManagement.entity.Product;
import com.SWP391.KoiManagement.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
                .message("Create successfully")
                .build();
    }
}
