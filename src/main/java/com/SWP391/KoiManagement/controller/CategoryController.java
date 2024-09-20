package com.SWP391.KoiManagement.controller;

import com.SWP391.KoiManagement.dto.response.ApiResponse;
import com.SWP391.KoiManagement.entity.Category;
import com.SWP391.KoiManagement.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/list")
    ApiResponse<List<Category>> getAllCategories() {
        return ApiResponse.<List<Category>>builder()
                .result(categoryService.getAllCategories())
                .message("Get categories successfully")
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<Category> getCategory(@PathVariable int id) {
        return ApiResponse.<Category>builder()
                .result(categoryService.getCategory(id))
                .message("Get category sucessfully")
                .build();
    }


}
