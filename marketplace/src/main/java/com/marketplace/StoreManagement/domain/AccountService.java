package com.marketplace.StoreManagement.domain;

import java.util.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.StoreRequestDTO;
import com.marketplace.StoreManagement.domain.StoreDetail.StoreStatusEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        Objects.requireNonNull(account);
        return accountRepository.save(account);
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
    public Store createStore(String sellerId, Role role, Boolean hasStoreNameSame, StoreRequestDTO storeDto) {
        Account account = getAccountById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("account with this id is not found " + sellerId));
        if (account.getStore() != null) {
            throw new IllegalArgumentException("The store has already been created");
        }
        if (hasStoreNameSame) {
            throw new ResourceDuplicationException("The name of the store is already been taken");
        }
        account.getAccountRoles().add(role);

        role.addSeller(account);

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
        return saveAccount(account).getStore();
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

    public Boolean doesAccountHaveStore(String sellerId) {
        Account account = accountRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User account with this id " + sellerId + " not found"));
        return account.getStore() != null;
    }

    public void deleteAccountAndStore(String sellerId) {
        Account account = getAccountById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with this id is not found " + sellerId));
        accountRepository.delete(account);
    }

}
