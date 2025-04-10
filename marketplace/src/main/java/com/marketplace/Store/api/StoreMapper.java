package com.marketplace.Store.api;

import org.springframework.stereotype.Component;

import com.marketplace.Store.domain.Account;
import com.marketplace.Store.domain.Store;
import com.marketplace.Store.domain.StoreDetail;

@Component
public class StoreMapper {
    
    public Store toStore(StoreRequestDTO storeDto, Account account, StoreDetail storeDetail) {
        return Store.builder()
            .name(storeDto.storeName())
            .account(account)
            .storeDetail(storeDetail)
            .build();
    }

    public StoreDto toStoreDto(Store store) {
        return new StoreDto(store.getName());
    }

}
