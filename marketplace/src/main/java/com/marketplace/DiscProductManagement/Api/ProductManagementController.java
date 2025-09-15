package com.marketplace.DiscProductManagement.Api;

import com.marketplace.DiscProductManagement.Domain.*;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductManagementController {

    private final StoreServiceImpl storeService;
    private final ProductMapper mapper;
    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryService;
    private final SubCategoryServiceImpl subCategoryService;

    @PostMapping("/stores/{store_id}/products")
    public ResponseEntity<Object> createProduct(@PathVariable("store_id") String storeId, @RequestBody ProductReqDto productDto) {
        Store store = storeService.getStore(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id is not found"));
        Category category = categoryService.getCategoryByName(productDto.categoryName());
        SubCategory subCategory = subCategoryService.getSubCategory(category);
        System.out.println("subCategory in controller is = " + subCategory);
        Product convertToProduct = mapper.toProduct(category, subCategory, store, productDto);
        Product product = productService.createProduct(convertToProduct);
        ProductDto output = mapper.toDto(product);
//        return output;
        return ResponseHandler.generateResponse("Successfully create product", HttpStatus.CREATED, output);
    }

    @GetMapping("/stores/{store_id}/products")
    public ResponseEntity<Object> getAllProductFromStore(@PathVariable("store_id") String storeId) {
        Store store = storeService.getStore(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id is not found"));
//        List<ProductDto> products = storeService.getStoreProduct(storeId, mapper);
//        storeService.getStoreProduct(storeId, mapper);
        List<ProductDto> products = productService.getAllProductsFromAStore(storeId);
        return ResponseHandler.generateResponse("Successfully get Product From the store", HttpStatus.OK, products);
    }

    @GetMapping("/stores/{store_id}/products/product_id")
    public ResponseEntity<Object> getSpecificProduct(@PathVariable("store_id") String storeId, @RequestParam("product_id") String productId) {
        storeService.getStore(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("The store with this id " + storeId + " not found"));
        ProductDto product = productService.getSpecificProduct(storeId, productId);
        return ResponseHandler.generateResponse("Successfully get product", HttpStatus.OK, product);
    }

    @DeleteMapping("/stores/{store_id}/products/product_id")
    public ResponseEntity<Object> deleteSpecificProduct(@PathVariable("store_id") String storeId, @RequestParam("product_id") String productId) {
        productService.deleteSpecificProduct(storeId, productId);

        return ResponseHandler.generateResponse("Successfully delete product", HttpStatus.OK, "");
    }


}
