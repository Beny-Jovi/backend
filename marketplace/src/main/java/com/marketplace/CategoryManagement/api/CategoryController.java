package com.marketplace.CategoryManagement.api;

import com.marketplace.CategoryManagement.domain.CategoryServiceImpl;
import com.marketplace.Util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("api/categories")
    public ResponseEntity<Object> getAllCategories() {
        List<CategoryDto> categoryDtos = categoryService.getCategories();
        if (categoryDtos.isEmpty()) {
            throw new IllegalArgumentException("Haven't created yet");
        }
        return ResponseHandler.generateResponse("", HttpStatus.OK, categoryDtos);
    }

    @PostMapping("admin/api/categories")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
//        categoryService.createCategories(categoryRequestDto);
        categoryService.createCategory(categoryRequestDto);
        return ResponseHandler.generateResponse("Category created", HttpStatus.CREATED, "");
    }

    @PostMapping("admin/api/many_categories")
    public ResponseEntity<Object> createCategories(@RequestBody @Valid List<CategoryRequestDto> categoryDtos) {
//        categoryService.createCategories(categoryDtos);
//        categoryService.getCategory(categoryDtos);
//        categoryService.categoryValidatorAndCreateCategories(categoryDtos);
//        categoryService.findAllCategory();
        return ResponseHandler.generateResponse("Category created", HttpStatus.CREATED, "");

    }

}
