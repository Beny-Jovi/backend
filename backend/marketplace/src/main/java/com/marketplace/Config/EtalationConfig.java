package com.marketplace.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marketplace.Etalation.EtalationMapper;
import com.marketplace.Etalation.EtalationSerivce;
import com.marketplace.Etalation.StoreService;

@Configuration
public class EtalationConfig {
    
    // @Bean
    // public StoreService storeService(){
    //     return new StoreService();
    // }

    @Bean
    public EtalationSerivce etalationSerivce(){
        return new EtalationSerivce();
    }

    @Bean
    public EtalationMapper etalationMapper() {
        return new EtalationMapper();
    }

}
