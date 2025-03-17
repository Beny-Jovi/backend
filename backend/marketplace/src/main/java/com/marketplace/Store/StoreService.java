package com.marketplace.Store;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// v3/api/seller/store
// v3/api/seller/store/id -> get

@Service("storeService")
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;

    public Store updateStore(Store foundStore, StoreRequestDTO storeDto) {
        foundStore.setName(storeDto.storeName());
        foundStore.setOperatingHoursStart(storeDto.operatingTimeStart());
        // foundStore.setStoreLogoPath(storeDto.storeLogoPath());
        storeRepository.save(foundStore);
        return foundStore;
    }
    
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public void deleteStoreById(Store foundStore) {
        storeRepository.delete(foundStore);
    }

    public void saveStore(Store store) {
        Objects.requireNonNull(store);
        storeRepository.save(store);
    }

    public Optional<Store> getStoreById(String storeId) {
        return storeRepository.findById(storeId);
    } 

    public Boolean hasStoreNameSame(String storeName) {
        return getAllStores().stream()
            .anyMatch(store -> store.getName().contains(storeName));
    }

    public List<StoreProjection> getStoresNameAndRate() {
        return storeRepository.findStoresNameAndRate();
    }

    // public List<StoreProjection> findStoresById(String accountId) {
    //     return storeRepository.findStoresByAccountId(accountId);
    // }

    // public Boolean isThereAStore(String id) {
    //     return getAllStores().stream()
    //         .anyMatch(store -> store.getId().contains(id));
    // }

}
