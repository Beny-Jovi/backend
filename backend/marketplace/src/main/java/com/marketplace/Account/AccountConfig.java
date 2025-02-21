package com.marketplace.Account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountConfig {
    
    @Bean
    public SellerService sellerService() {
        return new SellerService();
    }

}
