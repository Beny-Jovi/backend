package com.marketplace.CategoryManagement.api;

import com.marketplace.CategoryManagement.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        String categoryId = category.getId();
        String categoryName = category.getName();
        return new CategoryDto(categoryId, categoryName);
    }
}
