package com.marketplace.Store;

import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    
    public Store toStore(StoreRequestDTO storeDto) {
        return new Store(storeDto.storeName(), storeDto.operatingTimeStart(), storeDto.operatingTimeEnd(), "");
    }

    public StoreDto toStoreDto(Store store) {
        return new StoreDto(store.getName(), store.getRate(), store.getOperatingHoursStart(), store.getOperatingHoursEnd(), store.getStoreStatus(), store.getNumberOfSales());
    }

}
