package com.marketplace.Store;

import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    
    public Store toStore(StoreRequestDTO storeDto) {
        return new Store(storeDto.storeName(), storeDto.storeOperatingTimeStart(), storeDto.storeOperatingTimeEnd(), "");
    }

    public StoreDto toStoreDto(Store store) {
        return new StoreDto(store.getStoreName(), store.getStoreRate(), store.getStoreOperatingHoursStart(), store.getStoreOperatingHoursEnd(), store.getStoreStatus(), store.getNumberOfSales());
    }

}
