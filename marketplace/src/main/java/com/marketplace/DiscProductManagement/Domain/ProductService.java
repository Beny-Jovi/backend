package com.marketplace.DiscProductManagement.Domain;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProduct(String id);
    List<Product> getAllProducts();
//    Product createProduct();
//    List<Product> getAllProductsFromAStore(String id);
}
