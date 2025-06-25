package com.marketplace.DiscProductManagement;

import com.marketplace.DiscProductManagement.Domain.Store;
import com.marketplace.DiscProductManagement.Domain.StoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StoreDaoTest {

    @Autowired
    private StoreServiceImpl storeDao;

    private Store store;

    @BeforeEach
    public void setup() {
        String id = "store_id";
        String name= "store_name";

        store = Store.builder()
                .name(name)
                .build();
    }

    @RepeatedTest(3)
    @Order(1)
    public void saveStore_thenReturnStore() {
        String id = "store_id";
        String name= "store_name";

        Store savedStore = storeDao.saveStoreTest(name);

        assertThat(savedStore.getName()).isEqualTo(name);

    }

}
