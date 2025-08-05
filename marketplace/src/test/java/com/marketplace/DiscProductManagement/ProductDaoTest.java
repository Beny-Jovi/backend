package com.marketplace.DiscProductManagement;

import com.marketplace.DiscProductManagement.Api.ProductMapper;
import com.marketplace.DiscProductManagement.Api.ProductReqDto;
import com.marketplace.DiscProductManagement.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductDaoTest {

    @InjectMocks
    private ProductServiceImpl productService;
    @InjectMocks
    private StoreServiceImpl storeService;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @InjectMocks
    private SubCategoryServiceImpl subCategoryService;
    @InjectMocks
    private ProductMapper mapper;

    private Product product;

    @BeforeEach
    public void setup() {
        String id = "product_id";

        product = Product.builder()
                .orderMinimum(1)
                .createdAt(LocalDateTime.now())
                .build();
    }

//    @RepeatedTest(3)
//    @Order(1)
//    public void saveProductWithStore_thenReturnStoreName() {
//        System.out.println("product = " + product);
//
//        String storeName= "store_name";
//        Store savedStore = storeDao.saveStoreTest(storeName);
//        product.setStore(savedStore);
//        assertThat(savedStore.getName()).isEqualTo(storeName);
//
////        Category category = Category.builder()
////                .name(Category.CategoryEnum.DIGITAL_WORK)
////                .build();
//        Category savedCategory = categoryDao.getOrCreateCategoryByName();
//        System.out.println("category = " + savedCategory);
//        SubCategory savedSubCategory = subCategoryDao.getOrCreateSubCategory(savedCategory);
//        System.out.println("subCategory = " + savedSubCategory);
////        set the product for store
//        Set<Product> products = new HashSet<>();
//        System.out.println("products = " + products);
//        products.add(product);
//        System.out.println("products after added = " + products);
//        savedStore.setProducts(products);
//        System.out.println("savedStore products = " + savedStore.getProducts());
////        set sub savedCategory with sub savedCategory and vice versa
//        savedSubCategory.setCategory(savedCategory);
//        System.out.println("savedSubCategory.getCategory() = " + savedSubCategory.getCategory());
//        Set<SubCategory> subCategories = new HashSet<>();
//        subCategories.add(savedSubCategory);
//        System.out.println("subCategories = " + subCategories);
//        savedCategory.setSubCategories(subCategories);
//        System.out.println("savedCategory.getSubCategories() = " + savedCategory.getSubCategories());
////        System.out.println("savedSubCategory.getId() = " + savedSubCategory.getId());
//
////        set sub category with product and vice versa
//        product.setSubCategory(savedSubCategory);
//        savedSubCategory.addProduct(product);
//
////        save product
////        Product savedProduct = productDao.createProduct(product);
////        SubCategory savedSubCategory = subCategoryDao.getOrCreateSubCategory(category);
////        System.out.println("create product savedProduct = " + savedProduct);
//
////        verify the sub category
//        assertThat(savedSubCategory.getCategory().getName()).isEqualTo(Category.CategoryEnum.DIGITAL_WORK);
//        assertThat(savedSubCategory.getName()).isEqualTo(SubCategory.SubCategoryEnum.DISC);
//        assertThat(savedSubCategory.getProducts()).isNotNull();
//
//
////        verify the product
////        assertThat(savedProduct).isNotNull();
////        assertThat(savedProduct.getStore().getName()).isEqualTo(storeName);
////        assertThat(savedProduct.getSubCategory().getName()).isEqualTo(SubCategory.SubCategoryEnum.DISC);
////        assertThat(savedProduct.getOrderMinimum()).isGreaterThanOrEqualTo(1);
//
//    }

    @RepeatedTest(3)
    @Order(1)
    public void testSaveProductWithAssociations() {
        String storeName = "Test Store";
        Store savedStore = storeService.saveStoreTest(storeName);

        Category savedCategory = categoryService.getOrCreateCategoryByName();
        SubCategory savedSubCategory = subCategoryService.getOrCreateSubCategory(savedCategory);

        ProductReqDto productDto = new ProductReqDto(
                Category.CategoryEnum.DIGITAL_WORK,
                SubCategory.SubCategoryEnum.DISC,
                1,
                "Test Disc",
                Disc.ProductConditionEnum.NEW,
                "This is a test description",
                Disc.UnitOfProductEnum.KG,
                "Test Publisher"
        );

        ProductMapper mapper = new ProductMapper();
        Product productToSave = mapper.toProduct(savedCategory, savedSubCategory, savedStore, productDto);

        Product savedProduct = productService.createProduct(productToSave);

        assertThat(savedProduct.getStore().getName()).isEqualTo(storeName);
        // Optional: Additional assertions for robustness
        assertThat(savedProduct.getSubCategory().getName()).isEqualTo(SubCategory.SubCategoryEnum.DISC);

//        verify the sub category
        assertThat(savedSubCategory.getCategory().getName()).isEqualTo(Category.CategoryEnum.DIGITAL_WORK);
        assertThat(savedSubCategory.getName()).isEqualTo(SubCategory.SubCategoryEnum.DISC);
        assertThat(savedSubCategory.getProducts()).isNotNull();

    //        verify the product
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getStore().getName()).isEqualTo(storeName);
        assertThat(savedProduct.getSubCategory().getName()).isEqualTo(SubCategory.SubCategoryEnum.DISC);
        assertThat(savedProduct.getOrderMinimum()).isGreaterThanOrEqualTo(1);
    }


}
