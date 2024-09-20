package com.SWP391.KoiManagement.service;

import com.SWP391.KoiManagement.entity.Category;
import com.SWP391.KoiManagement.exception.AppException;
import com.SWP391.KoiManagement.exception.ErrorCode;
import com.SWP391.KoiManagement.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll().stream().toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Category getCategory(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
    }
}
