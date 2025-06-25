package com.marketplace.DiscProductManagement;

import com.marketplace.DiscProductManagement.Domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Autowired
    private ProductServiceImpl productDao;
    @Autowired
    private StoreServiceImpl storeDao;
    @Autowired
    private CategoryServiceImpl categoryDao;
    @Autowired
    private SubCategoryServiceImpl subCategoryDao;

    private Product product;

    @BeforeEach
    public void setup() {
        String id = "product_id";

        product = Product.builder()
                .orderMinimum(1)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @RepeatedTest(3)
    @Order(1)
    public void saveProductWithStore_thenReturnStoreName() {
        System.out.println("product = " + product);
        
        String storeName= "store_name";
        Store savedStore = storeDao.saveStoreTest(storeName);
        product.setStore(savedStore);

        Category category = Category.builder()
                .name(Category.CategoryEnum.DIGITAL_WORK)
                .build();


        SubCategory subCategory = SubCategory.builder()
                .name(SubCategory.SubCategoryEnum.DISC)
                .build();

//        set the product for store
        Set<Product> products = new HashSet<>();
        System.out.println("products = " + products);
        products.add(product);
        System.out.println("products after added = " + products);
        savedStore.setProducts(products);

//        set sub category with sub category and vice versa
        subCategory.setCategory(category);
        Set<SubCategory> subCategories = new HashSet<>();
        subCategories.add(subCategory);
        category.setSubCategories(subCategories);
//        System.out.println("savedSubCategory.getId() = " + savedSubCategory.getId());

//        set sub category with product and vice versa
        product.setSubCategory(subCategory);
        subCategory.addProduct(product);

//        save product
        Product savedProduct = productDao.createProduct(product);
        SubCategory savedSubCategory = subCategoryDao.getOrCreateSubCategory(category);
        System.out.println("savedProduct = " + savedProduct);

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
