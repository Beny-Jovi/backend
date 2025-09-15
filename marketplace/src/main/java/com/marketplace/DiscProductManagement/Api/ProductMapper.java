package com.marketplace.DiscProductManagement.Api;

import com.marketplace.DiscProductManagement.Domain.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ProductMapper {

    public Product toProduct(Category category, SubCategory subCategory,Store store, ProductReqDto productDto) {

        Disc disc = Disc.builder()
                .unitOfProduct(productDto.unitOfProduct())
                .title(productDto.discTitle())
                .productCondition(productDto.productCondition())
                .description(productDto.description())
                .publisher(productDto.publisher())
                .build();

        ProductView productView = ProductView.builder()
                .productRate(0)
                .numberOfView(0)
                .build();
        Product product = new Product(productDto.productOrderMinimum(), store, productView, disc, subCategory);
        disc.setProduct(product);
        product.setDisc(disc);
        System.out.println("subCategory = " + subCategory);
        System.out.println("subCategory.getId() = " + subCategory.getId());
        System.out.println("product.getOrderMinimum() = " + product.getOrderMinimum());
        System.out.println("product.getSubCategory = " + product.getSubCategory());
        
        Set<Product> products = new HashSet<>();
        products.add(product);
        System.out.println("products = " + products);
        subCategory.setProducts(products);
        Set<SubCategory> subCategories = new HashSet<>();
        subCategories.add(subCategory);
        category.setSubCategories(subCategories);

        subCategory.setCategory(category);

        productView.setProduct(product);
        product.setProductView(productView);
        return product;
    }

    public ProductDto toDto(Product product) {
        System.out.println("converted product to dto = " + product);
        String productId = product.getId();
        String productCategory = product.getSubCategory().getCategory().getName();
        SubCategory.SubCategoryEnum productSubCategory = product.getSubCategory().getName();
        Disc.UnitOfProductEnum unitOfProduct = product.getDisc().getUnitOfProduct();
        String discTitle = product.getDisc().getTitle();
        Disc.ProductConditionEnum productCondition = product.getDisc().getProductCondition();
        String productDesc = product.getDisc().getDescription();
        String publisher = product.getDisc().getPublisher();
        int productRate = product.getProductView().getProductRate();
        int productNumOfView = product.getProductView().getNumberOfView();
        int productOrderMinimum = product.getOrderMinimum();

        return new ProductDto(productId, productCategory, productSubCategory, productOrderMinimum, discTitle, productCondition, productDesc, unitOfProduct, publisher, productRate, productNumOfView);
    }

}
