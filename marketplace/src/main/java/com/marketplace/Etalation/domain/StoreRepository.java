package com.marketplace.Etalation.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("EtalationStoreRepository")
public interface StoreRepository extends JpaRepository<Store, String> {
    
}
