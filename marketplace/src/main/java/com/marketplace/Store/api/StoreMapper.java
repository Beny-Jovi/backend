package com.marketplace.Store.api;

import org.springframework.stereotype.Component;

import com.marketplace.Store.domain.Store;

@Component
public class StoreMapper {
    
    public Store toStore(StoreRequestDTO storeDto) {
        return new Store(storeDto.storeName(), storeDto.operatingTimeStart(), storeDto.operatingTimeEnd(), "");
    }

    public StoreDto toStoreDto(Store store) {
        return new StoreDto(store.getName(), store.getRate(), store.getOperatingHoursStart(), store.getOperatingHoursEnd(), store.getStoreStatus().name(), store.getNumberOfSales());
    }

}
