package com.marketplace.DiscProductManagement.Api;

import com.marketplace.DiscProductManagement.Domain.Category;
import com.marketplace.DiscProductManagement.Domain.Disc;
import com.marketplace.DiscProductManagement.Domain.SubCategory;

public record ProductDto(Category.CategoryEnum categoryName, SubCategory.SubCategoryEnum subCategoryName, Integer productOrderMinimum, String discTitle, Disc.ProductConditionEnum productCondition, String description, Disc.UnitOfProductEnum unitOfProduct, String publisher, int productRate, int productNumOfView) {
}
