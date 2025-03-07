package com.marketplace.Store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreConfig {
    
    @Bean
    public StoreService storeService() {
        return new StoreService();
    }

    @Bean
    public AccountService accountService() {
        return new AccountService();
    }

    @Bean StoreMapper mapper() {
        return new StoreMapper();
    }

}
