package com.marketplace.Account;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SellerAccountConfig {
    
    @Bean
    public SellerService sellerService() {
        return new SellerService();
    }

    @Bean
    public RoleService roleService() {
        return new RoleService();
    }

    @Bean
    public SellerMapper sellerMapper() {
        return new SellerMapper();
    }

}
