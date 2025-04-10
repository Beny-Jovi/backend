package com.marketplace.Store;

import com.marketplace.Store.api.StoreDto;

import static  org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class StoreDtoTest {

    @Test
    void shouldCreateStoreDto() {
        StoreDto test = new StoreDto("test");
        assertEquals("test", test.storeName(), "this name is a test");
    }
}
