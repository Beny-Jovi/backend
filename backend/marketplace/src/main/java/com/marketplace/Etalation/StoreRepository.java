package com.marketplace.Etalation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("etalationStoreRepository")
public interface StoreRepository extends JpaRepository<Store, String> {
    
}
