package com.marketplace.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marketplace.Account.RoleService;
import com.marketplace.Account.SellerMapper;
import com.marketplace.Account.SellerService;

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
