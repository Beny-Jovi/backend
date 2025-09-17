package com.marketplace.CategoryManagement;

import com.marketplace.CategoryManagement.api.CategoryRequestDto;
import com.marketplace.CategoryManagement.domain.Category;
import com.marketplace.CategoryManagement.domain.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CategoryServiceUnitTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private String name;

    @BeforeEach
    protected void setup() {
        name = "category_test";
        category = new Category(name);
    }

    @RepeatedTest(3)
    public void saveCategory_andReturnCategory() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name);
        Category category1 = categoryService.createCategory(categoryRequestDto);
        assertThat(category1.getName()).isEqualTo(name);
    }
}
