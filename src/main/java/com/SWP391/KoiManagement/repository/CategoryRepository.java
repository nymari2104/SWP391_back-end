package com.SWP391.KoiManagement.repository;

import com.SWP391.KoiManagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsById(int id);
}
