package com.marketplace.DiscProductManagement.Domain;

import com.marketplace.DiscProductManagement.Api.ProductDto;
import com.marketplace.DiscProductManagement.Api.ProductMapper;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    Optional<Store> getStore(String id);
    void deleteProduct(String storeId);
//    List<ProductDto> getStoreProduct(String storeId, ProductMapper mapper);

}
