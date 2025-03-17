package com.marketplace.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marketplace.Store.AccountService;
import com.marketplace.Store.StoreMapper;
import com.marketplace.Store.StoreService;

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
