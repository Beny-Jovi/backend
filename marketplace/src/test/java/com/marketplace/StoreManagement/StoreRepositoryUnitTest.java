package com.marketplace.StoreManagement;

import com.marketplace.StoreManagement.api.StoreProjection;
import com.marketplace.StoreManagement.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StoreRepositoryUnitTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RepeatedTest(3)
    @DisplayName("Test 1 : Save Store test")
    @Order(1)
    @Transactional
    @Rollback(value = false)
    public void saveAccountAndStore_thenReturnStoreId() {

        Account account = Account.builder()
                .email("account@test")
                .password("test_password")
                .name("testAccount")
                .build();
        StoreDetail storeDetail = StoreDetail.builder()
                .numberOfSales(0)
                .rate(0)
                .status(StoreDetail.StoreStatusEnum.BRONZE)
                .build();
        Profile  storeProfile = new Profile();
        storeProfile.setLogoPath("test_logo_path");
        storeProfile.setCreatedAt(LocalDateTime.now());
        storeProfile.setUpdatedTimes(LocalDateTime.now());
        Store store = Store.builder()
                .name("things")
                .account(account)
                .storeProfile(storeProfile)
                .storeDetail(storeDetail)
                .build();
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedTimes(LocalDateTime.now());
        storeDetail.setStore(store);
        account.setStore(store);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedTimes(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        store.setAccount(account);
        Store savedStore = storeRepository.save(store);

        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getName()).isEqualTo("testAccount");
        assertThat(savedAccount.getStore()).isNotNull();
        assertThat(savedAccount.getEmail()).isEqualTo("account@test");

        assertThat(savedStore).isNotNull();
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore.getName()).isEqualTo("things");

        assertThat(savedStore.getStoreDetail()).isNotNull();
        assertThat(savedStore.getStoreDetail().getRate()).isEqualTo(0);
        assertThat(savedStore.getStoreDetail().getStatus()).isEqualTo(StoreDetail.StoreStatusEnum.BRONZE);
        assertThat(savedStore.getStoreDetail().getNumberOfSales()).isEqualTo(0);

        assertThat(savedStore.getStoreProfile()).isNotNull();
        assertThat(savedStore.getStoreProfile().getLogoPath()).isEqualTo("test_logo_path");

    }

    @RepeatedTest(3)
    @Order(2)
    public void testGetAllStore_thenReturnListStoreIdAndStoreName() {
        List<Store> stores = storeRepository.findAll();

        assertThat(stores).isNotNull();
        assertThat(stores.getFirst().getName()).isEqualTo("things");

        List<StoreProjection> storeProjections = storeRepository.getAllStore();

        assertThat(storeProjections).isNotNull();
        assertThat(storeProjections.getFirst()).isNotNull();
        assertThat(storeProjections.getFirst().storeId()).isEqualTo(stores.getFirst().getId());
        assertThat(storeProjections.getFirst().storeName()).isEqualTo("things");
    }
}
