package com.marketplace.Store.domain;

import java.util.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store.api.StoreRequestDTO;
import com.marketplace.Store.domain.StoreDetail.StoreStatusEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public void saveAccount(Account account) {
        Objects.requireNonNull(account);
        accountRepository.save(account);
    }

    public void deleteStoreFromAccount(String sellerId) {
        Account account = getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller with this id not found"));
        if (account.getStore() == null) {
            throw new IllegalArgumentException("Store has not created yet");
        }
        account.setStore(null);
        saveAccount(account);
    }

    @Transactional
    public Store createStore(String sellerId, Boolean hasStoreNameSame, StoreRequestDTO storeDto) {
        Account account = getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("account with this id is not found " + sellerId));
        if (account.getStore() != null) {
            throw new IllegalArgumentException("The store has already been created");
        }
        if (hasStoreNameSame) {
            throw new ResourceDuplicationException("The name of the store is already been taken");
        }

        StoreDetail storeDetail = StoreDetail.builder()
            .rate(0)
            .numberOfSales(0)
            .status(StoreStatusEnum.BRONZE)
            .build();
        Store createdStore = Store.builder()
            .name(storeDto.storeName())
            .storeDetail(storeDetail)
            .build();
        Profile storeProfile = new Profile(createdStore);

        account.setStore(createdStore);
        createdStore.setAccount(account);
        createdStore.setStoreProfile(storeProfile);
        storeDetail.setStore(createdStore);

        saveAccount(account);
        return createdStore;
    }

    public String updateStoreName(String sellerId, Boolean hasStoreNameSame, StoreRequestDTO storeDto) {
        Account account = getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller with this id is not found " + sellerId));
        if (account.getStore() == null) {
            throw new IllegalArgumentException("Store has not created yet");
        }
        if (hasStoreNameSame) {
            throw new ResourceDuplicationException("The name of the store is already been taken");
        }
        Store createdStore = account.getStore();
        createdStore.setName(storeDto.storeName());
        saveAccount(account);
        return createdStore.getName();
    }

}
