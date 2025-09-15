package com.marketplace.DiscProductManagement.Api;

import com.marketplace.DiscProductManagement.Domain.Category;
import com.marketplace.DiscProductManagement.Domain.Disc;
import com.marketplace.DiscProductManagement.Domain.SubCategory;

public record ProductReqDto(String categoryName, SubCategory.SubCategoryEnum subCategoryName, int productOrderMinimum, String discTitle, Disc.ProductConditionEnum productCondition, String description, Disc.UnitOfProductEnum unitOfProduct, String publisher) {
}
