package com.marketplace.DiscProductManagement.Domain;

public interface SubCategoryService {
    SubCategory getSubCategory(Category category, String subCategoryName);
}
