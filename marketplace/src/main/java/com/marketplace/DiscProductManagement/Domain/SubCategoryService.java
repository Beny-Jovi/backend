package com.marketplace.DiscProductManagement.Domain;

public interface SubCategoryService {
    SubCategory getOrCreateSubCategory(Category category);
}
